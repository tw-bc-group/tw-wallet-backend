.PHONY: build

APP	      := tw-wallet-backend
TAG       ?= tw-wallet:latest

CACHE     := $(shell pwd | md5sum | awk '{ print "$(APP)-"$$1 }')
JDK_IMAGE := openjdk:8u212-jdk-alpine3.9
OPTS       = --rm
OPTS	    +=  -u $(shell id -u) 
OPTS      +=  -v $(shell pwd):/mnt
OPTS      +=  -w /mnt

FLAG      ?= --info
gradle    := docker run ${OPTS} ${JDK_IMAGE} ./gradlew --no-daemon $(flag)

cache:
	@echo $(OPTS)
	@echo cache volume creating
	docker volume create $(CACHE)
	@echo cache volume done

clean:
	@echo clean start
	$(gradle) clean
	@echo clean done 

build:
	@echo build start
	$(gradle) clean build -x test -x generateWalletJooqSchemaSource
	@echo build done

image:
	@echo build $(TAG)
	docker build -t $(TAG) .
