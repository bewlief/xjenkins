all: build deploy
ci: build-ci login-ci deploy
deploy-ci: login-ci deploy

.PHONY: build deploy build-ci
build:
		docker build --build-arg aopdemo.proxy=http://docker.for.mac.host.internal:3128 . -t docker-registry-default.apps.omni.service.test/anz-apply-dev-tools/cb-ghe-slack-notifier

deploy:
		docker push docker-registry-default.apps.omni.service.test/anz-apply-dev-tools/cb-ghe-slack-notifier

build-ci:
		docker build --build-arg aopdemo.proxy=${http_proxy} . -t docker-registry-default.apps.omni.service.test/anz-apply-dev-tools/cb-ghe-slack-notifier

login-ci:
		@docker login -u serviceaccount -p ${oc_registry_token} docker-registry-default.apps.omni.service.test | grep "Succeeded" && echo "Successful Login!" || (echo "[ERROR] Login failed." && exit 1)
