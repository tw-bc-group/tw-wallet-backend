def COLOR_MAP = [
        'SUCCESS': 'good',
        'FAILURE': 'danger',
]

pipeline {
    agent any
    environment {
        DB_USERNAME = "${DB_USERNAME}"
        DB_PASSWORD = "${DB_PASSWORD}"
        DB_URL = "${DB_URL}"
        NODE1_PRIVATE_KEY = "${NODE1_PRIVATE_KEY}"
        TWPOINT_CONTRACT_ADDRESS = "${TWPOINT_CONTRACT_ADDRESS}"
        IDENTITY_REGISTRY_CONTRACT_ADDRESS = "${IDENTITY_REGISTRY_CONTRACT_ADDRESS}"
        HEALTH_VERIFICATION_CLAIM_CONTRACT_ADDRESS = "${HEALTH_VERIFICATION_CLAIM_CONTRACT_ADDRESS}"
        HEALTH_VERIFICATION_CLAIM_ISSUER_ADDRESS = "${HEALTH_VERIFICATION_CLAIM_ISSUER_ADDRESS}"
        HEALTH_VERIFICATION_CLAIM_ISSUER_PRIVATE_KEY = "${HEALTH_VERIFICATION_CLAIM_ISSUER_PRIVATE_KEY}"
        RPC_URL = "${RPC_URL}"
        DOCKER_REG = "${BC_DOCKER_REG}"
        LOG_DIR = "${LOG_DIR}"
        HOST_IP = "${HOST_IP}"
        TW_WALLET_IMAGE = "${BC_DOCKER_REG}/tw-wallet:build-${BUILD_NUMBER}"
    }
    stages {
        stage('Migration') {
            steps {
                sh './gradlew --no-daemon -Penv=dev flywayMigrate'
            }
        }

        stage('Build') {
            steps {
                // sh 'make cache'
                // sh 'make build'
                // TODO: Remove this workaround
                sh './gradlew --no-daemon -Penv=dev clean build -x integTest'
            }
        }

<<<<<<< HEAD
        stage('Dockerize') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'wallet-docker-account', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USER')]) {
                    sh '''
                    echo $DOCKER_PASSWORD | docker login --username $DOCKER_USER --password-stdin $DOCKER_REG
                    '''
                }
                sh 'make image TAG=$TW_WALLET_IMAGE'
                sh 'docker push $TW_WALLET_IMAGE'
                sh 'docker rmi $TW_WALLET_IMAGE'
            }
        }
=======
    stage('Dockerize') {
      steps {
        sh 'aws ecr get-login-password | docker login  -u AWS --password-stdin $DOCKER_REG'
        sh 'make image TAG=$TW_WALLET_IMAGE'
        sh 'docker push $TW_WALLET_IMAGE'
        sh '/usr/local/bin/kompose convert -c -f docker/docker-compose.yml'
        sh 'cd docker; /usr/local/bin/docker-compose up -d'
      }
    }
>>>>>>> 4b0c5923ba05ef07fe26f9dcef0783ce3c7a3579

        stage('Deploy') {
            steps {
                sh 'env > .env'
                sh 'make deploy.sync TAG=$TW_WALLET_IMAGE'
                sh 'make deploy.webapp TAG=$TW_WALLET_IMAGE'
                sh 'make deploy.webapp.service'
            }
        }
    }

    post {
        always {
            echo 'Time to send slack message.'
            slackSend channel: '#wallet',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} on (<${env.BUILD_URL}|Open>)"
        }
    }
}
