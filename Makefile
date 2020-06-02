.PHONY: build k8s.namespace

APP	      := tw-wallet-backend
TAG       ?= tw-wallet:latest

CACHE     := $(shell pwd | md5sum | awk '{ print "$(APP)-"$$1 }')
JDK_IMAGE := openjdk:8u212-jdk-alpine3.9
OPTS       = --rm
# OPTS	    +=  -u $(shell id -u) 
OPTS      +=  -v $(CACHE):/home/gradle/.gradle
OPTS      +=  -v $(shell pwd):/home/gradle/project
OPTS      +=  -w /home/gradle/project

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
	$(gradle) clean build -x integTest
	@echo build done

image:
	@echo build $(TAG)
	docker build -t $(TAG) .

k8s.namespace:
	kubectl apply -f docker/k8s/tw-wallet-namespace.yaml

k8s.resetconfig:
	kubectl -n tw-wallet delete configmap tw-wallet-env

k8s.configmap:
	kubectl -n tw-wallet create configmap tw-wallet-env --from-env-file=.env

k8s.showconfig:
	kubectl -n tw-wallet get configmap tw-wallet-env -o yaml

k8s.show:
	kubectl -n tw-wallet get all

deploy.sync:
	cat docker/k8s/tw-wallet-sync-deployment.yaml \
		| sed 's#tw-wallet:latest#${TAG}#' \
		| kubectl apply -n tw-wallet -f -

deploy.webapp:
	cat docker/k8s/tw-wallet-webapp-deployment.yaml \
		| sed 's#tw-wallet:latest#${TAG}#' \
		| kubectl apply -n tw-wallet -f -

deploy.webapp.service:
	cat docker/k8s/tw-wallet-webapp-service.yaml \
		| kubectl apply -n tw-wallet -f -