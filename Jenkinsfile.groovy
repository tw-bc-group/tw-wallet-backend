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
        DOCKER_REG = "${DOCKER_REGISTRY}"
        LOG_DIR = "${LOG_DIR}"
        HOST_IP = "${HOST_IP}"
        IMAGE_NAME = "${DOCKER_REGISTRY}/tw-wallet"
        IMAGE_TAG  = "build-${BUILD_NUMBER}"
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
                sh './gradlew --no-daemon -Penv=dev clean build -x test'
            }
        }

        stage('Dockerize') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Push') {
            steps {
                sh '''
                export AWS_PROFILE=tw-bc-cn
                aws ecr get-login-password | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
                docker push ${IMAGE_NAME}:${IMAGE_TAG}
                docker rmi ${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh 'env > .env'
                sh 'kubectl get namespaces tw-wallet-backend-ns-dev || kubectl create namespace tw-wallet-backend-ns-dev'
                sh 'helm -n tw-wallet-backend-ns-dev upgrade --install tw-wallet-backend ./helm --set image.tag=${IMAGE_TAG} --set image.repository=${IMAGE_NAME}'
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
