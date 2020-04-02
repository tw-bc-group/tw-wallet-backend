pipeline {
    agent any
    stages {
        stage('Clean') {
            steps {
                sh './ci/clean.sh'
            }
        }

        stage('Build') {
            steps {
                sh './ci/build.sh'
            }
        }

        stage('Dockerize') {
            steps {
                sh './ci/dockerize.sh'
            }
        }
    }
}
