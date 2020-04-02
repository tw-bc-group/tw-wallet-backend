pipeline {
  agent any
  stages {
    stage('Build') {
            steps {
                sh 'make cache'
                sh 'make build'
            }
        }

    stage('Dockerize') {
        environment {
          DOCKER_REG = "${DOCKER_REG}"
          IMAGE = "${DOCKER_REG}/tw-wallet:build-${BUILD_NUMBER}"
        }
        steps {
            sh 'aws ecr get-login-password | docker login  -u AWS --password-stdin $DOCKER_REG'
            sh 'make dockerize image=$IMAGE'
            sh 'docker push $IMAGE'
        }
    }
  }
}
