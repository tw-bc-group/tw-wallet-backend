.PHONY: build k8s.namespace

APP	      := tw-wallet-backend
TAG       ?= tw-wallet:latest
NAME_SPACE := tw-wallet-backend-ns

CACHE     := $(shell pwd | shasum | awk '{ print "$(APP)-"$$1 }')
JDK_IMAGE := openjdk:8u212-jdk-alpine3.9
OPTS       = --rm
# OPTS	    +=  -u $(shell id -u) 
OPTS      +=  -v $(CACHE):/home/gradle/.gradle
OPTS      +=  -v $(shell pwd):/home/gradle/project
OPTS      +=  -w /home/gradle/project
OPTS      +=  --env-file .env.local

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
boot:
	@echo springboot run
	$(gradle) :tw-wallet-webapp:bootRun -x integTest
	@echo springboot end
build:
	@echo build start
	$(gradle) clean build -x integTest
	@echo build done

k8s.namespace:
	kubectl apply -f docker/k8s/tw-wallet-namespace.yaml

k8s.resetconfig:
	kubectl -n $(NAME_SPACE) delete configmap tw-wallet-env

k8s.configmap:
	kubectl -n $(NAME_SPACE) create configmap tw-wallet-env --from-env-file=.env

k8s.showconfig:
	kubectl -n $(NAME_SPACE) get configmap tw-wallet-env -o yaml

k8s.logs:
	kubectl -n $(NAME_SPACE) logs $(pod) -f

k8s.show:
	kubectl -n $(NAME_SPACE) get all
