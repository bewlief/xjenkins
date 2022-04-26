tag=$(shell cat .version)
repo=omni-releases.artifactory.gcp.anz
dtr_registry=dtrqa.docker.service.anz
dtr_repo=dse-prepop-bff-prv-auth-qa1
image_type=csp
image=colabff
TAG_NAME=$(repo)/$(image_type)/$(image):$(tag)
DTR_TAG_NAME=$(dtr_registry)/$(dtr_repo)/$(image):$(tag)
# TODO this only specify the CI env, how to do others?
TARGET_ENV=ci

.PHONY: version
version:
	$(shell ./scripts/upgrade-version.sh ${PATCHNUM})
	TAG_NAME=$(repo)/$(image_type)/$(image):$(tag)
	echo $(TAG_NAME)

.PHONY: build
build:
	docker build --build-arg APP_PROP_FILE=${TARGET_ENV}  -t $(TAG_NAME) .
	docker tag $(TAG_NAME) $(DTR_TAG_NAME)

.PHONY: docker-push
docker-push:
	docker login -u="${username}" -p="${password}" $(repo)
	docker image ls
	docker push $(TAG_NAME)
	@docker login -u="${DTR_USERNAME}" -p="${DTR_PASSWORD}" $(dtr_registry)
	docker push $(DTR_TAG_NAME)

.PHONY: tag
tag:
	@echo $(TAG_NAME)

.PHONY: build-version
build-version:
	@echo $(tag)

.PHONY: generate-manifests
generate-manifests:
	make -C ./openshift/
	cd openshift/output && sed -i 's/<replace-tag-version>/${BUILD_VERSION}/g' * && cd ../..
	zip -j deployment-manifests.zip openshift/output/*

.PHONY: deploy
deploy:
	./scripts/deploy-to-cpaas.sh

.PHONY: removed
removed:
    # // ./gradlew clean build --refresh-dependencies