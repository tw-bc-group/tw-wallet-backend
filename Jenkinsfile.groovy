pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh './gradlew clean test'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build --no-daemon'
            }
        }

        stage('Publish') {
            steps {
                // TO DO
                sh 'echo \'publish script\''
            }
        }

        stage('API test') {
            steps {
                // TO DO
                sh 'echo \'API test script\''
            }
        }

        stage('Deploy') {
            steps {
                // TO DO
                sh 'echo \'Deploy script\''
            }
        }
    }
}
