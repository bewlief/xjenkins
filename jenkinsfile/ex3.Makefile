CORE_BRANCH=$(shell cat .corebranch)
VERSION=$(shell cat ./plugin/.version)

.PHONY: prepare
prepare:
	git clone \
		--single-branch \
		--branch ${CORE_BRANCH} \
		https://github.service.xhoe/csp/processorchestration processorchestration

.PHONY: build/plugin
build/plugin:
	./gradlew clean build --refresh-dependencies -PcoreProjectDir=processorchestration


.PHONY: build/schema-bundle
build/schema-bundle:
	./gradlew bundleZip -PcoreProjectDir=processorchestration

.PHONY: publish
publish:
	./gradlew \
		-PenvName=ci \
		-PartifactoryUsername=${ARTIFACTORY_USERNAME} \
		-PartifactoryPassword="${ARTIFACTORY_PASSWORD}" \
		-PcoreProjectDir=processorchestration \
		:plugin:publish

.PHONY: tag
tag:
	./gradlew createTag \
        -PgitUsername=${GITHUB_APP} \
        -PgitPassword=${GITHUB_TOKEN} \
        -PcoreProjectDir=processorchestration \
        -PpatchNum=${PATCHNUM}
