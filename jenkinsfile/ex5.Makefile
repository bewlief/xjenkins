PROJECT_NAME = $(shell node -p "try { require('./package.json').name } catch(e) {}")
HASH = $(shell git rev-parse --short HEAD)
BRANCH = $(shell git rev-parse --abbrev-ref HEAD)
VERSION = $(shell git describe --tags)

echo 'PROJECT_NAME:' $PROJECT_NAME
echo 'HASH:' $HASH
echo 'BRANCH:' $BRANCH
echo 'VERSION:' $VERSION

.PHONY: prepare
prepare:
	yarn install

.PHONY: build
build:
	yarn build
	(cd build; zip -r ../$(PROJECT_NAME)-$(VERSION).zip *)

.PHONY: check
check:
	yarn lint

.PHONY: test
test:
	# TODO Automated tests
	# yarn test-ci 

.PHONY: clean
clean:
	rm -rf build

.PHONY: pipeline
pipeline:
	@make clean
	@make prepare
	@git diff-index --quiet HEAD -- || (echo "Staging area is not empty. Please commit your changes."; exit 1)
	@make check
	@make build