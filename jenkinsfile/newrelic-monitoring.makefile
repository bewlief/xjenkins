SHELL := /bin/bash
USER := $(shell whoami)
ifeq ($(USER),jenkins)
HOME :=	/home/$(USER)
endif


export https_proxy := $(subst localhost,host.docker.internal,$(https_proxy))
export HTTPS_PROXY := $(https_proxy)
export HTTP_PROXY := $(https_proxy)
export http_proxy := $(https_proxy)
export https_proxy := $(https_proxy)
export no_proxy := $(no_proxy)
export TF_DATA := .terraform.d
export TF_VAR_env ?= nonprod
export TF_LOG_PATH := /tmp/tflogs
export TF_LOG := TRACE
export TF_IN_AUTOMATION := true
export CHECKPOINT_DISABLE := true
export TF_VAR_newrelic_api_key := $(TF_VAR_newrelic_api_key)
TF_PLUGINS ?= $(TF_DATA)/plugins
GIT_BRANCH ?= $(shell git rev-parse --abbrev-ref HEAD)

TF_STATE_REPO ?= git@github.service.anz:dsso/newrelic-monitoring-states.git
TF_STATE_DIR ?= /tmp/tfstate
TF_STATE_FILE ?= $(TF_STATE_DIR)/$(TF_VAR_env)/terraform.tfstate
TF_CHANGE_PLAN ?= /tmp/terraform-changes.plan
ifeq ($(wildcard $(TF_STATE_FILE)),$(TF_STATE_FILE))
GIT_CMD := -C $(TF_STATE_DIR) pull
else
GIT_CMD := clone $(TF_STATE_REPO)  --branch master --single-branch $(TF_STATE_DIR)
endif
export TF_CLI_ARGS_init := -plugin-dir $(TF_PLUGINS) -backend-securitydemo.config='path=$(TF_STATE_FILE)'
export TF_CLI_ARGS_plan := -state=$(TF_STATE_FILE) -no-color -detailed-exitcode -out=$(TF_CHANGE_PLAN)
export TF_CLI_ARGS_apply := -state=$(TF_STATE_FILE) -no-color --auto-approve
$(info TF State File : $(TF_STATE_FILE))
$(info TF State Synch Command : git $(GIT_CMD))
$(info TF CLI Init : $(TF_CLI_ARGS_init))
$(info TF CLI Plan : $(TF_CLI_ARGS_plan))
$(info GIT Branch : $(GIT_BRANCH))

.PHONY: agent plan init apply

plan: init lint
	@if [ -z "$(TF_VAR_newrelic_api_key)" ]; \
	then \
		echo New Relic API Key not defined. Please set environment variable TF_VAR_newrelic_api_key; \
		exit 1; \
	fi
	@terraform plan  ./terraform/$(TF_VAR_env); \
	if [ $$? -eq 2 ]; \
	then \
		exit 0; \
	else \
		if [ $$? -eq 0 ]; \
		then \
			echo 'No changes to be applied, quitting'; \
			rm -f $(TF_CHANGE_PLAN); \
		fi; \
	fi	

apply:
ifeq ($(GIT_BRANCH),master)
	@if [ -f $(TF_CHANGE_PLAN) ]; \
	then \
		terraform apply	$(TF_CHANGE_PLAN)
	else
		echo 'No changes to be applied, quitting'; \
		exit 0; \	
	fi
else
	@echo 'deployments can only be carried out from the master branch'	
endif

lint: | npm-securitydemo.config
	npm run lint

validate-synthetics: | npm-securitydemo.config
	find resources/ -type f -exec node newrelic_browser_simulator.js {} \;

npm-securitydemo.config:
	npm securitydemo.config set https-aopdemo.proxy http://$(HTTPS_PROXY)
	npm install --quiet --silent --no-progress

init: $(TF_STATE_FILE)
	@terraform init -no-color ./terraform/$(TF_VAR_env)

$(TF_STATE_FILE):
	git $(GIT_CMD)

agent:
	@docker-compose run agent