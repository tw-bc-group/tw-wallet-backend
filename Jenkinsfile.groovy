pipeline {
  agent any
  environment {
    DB_USERNAME     = "${DB_USERNAME}"
    DB_PASSWORD = "${DB_PASSWORD}"
    DB_URL = "${DB_URL}"
    NODE1_PRIVATE_KEY = "${NODE1_PRIVATE_KEY}"
    TWPOINT_CONTRACT_ADDRESS = "${TWPOINT_CONTRACT_ADDRESS}"
    IDENTITY_REGISTRY_CONTRACT_ADDRESS = "${IDENTITY_REGISTRY_CONTRACT_ADDRESS}"
    RPC_URL = "${RPC_URL}"
    DOCKER_REG = "${DOCKER_REG}"
    LOG_DIR = "${LOG_DIR}"
    TW_WALLET_IMAGE = "${DOCKER_REG}/tw-wallet:build-${BUILD_NUMBER}"
  }
  stages {
    stage('Build') {
      steps {
        // sh 'make cache'
        // sh 'make build'
        // TODO: Remove this workaround
        sh './gradlew --no-daemon -Penv=dev clean build -x integTest'
      }
    }

    stage('Dockerize') {
      steps {
        sh 'aws ecr get-login-password | docker login  -u AWS --password-stdin $DOCKER_REG'
        sh 'make image TAG=$TW_WALLET_IMAGE'
        sh 'docker push $TW_WALLET_IMAGE'
        sh '/usr/local/bin/kompose convert -c -f docker/docker-compose.yml'
        sh 'cd docker; /usr/local/bin/docker-compose up -d'
      }
    }

    stage('Deploy') {
      steps {
        sh '/usr/local/bin/kompose convert -c -f docker/docker-compose.yml'
        // TODO: Remove this will real helm install
        sh 'env > docker/.env'
        sh 'cd docker; /usr/local/bin/docker-compose up -d'
      }
    }
  }
}



