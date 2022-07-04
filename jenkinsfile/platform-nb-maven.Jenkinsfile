pipeline {
    agent none

    stages {
        stage('Deployment') {
            agent { docker 'maven:3-alpine' }
            steps {
                sh 'mvn --version'
                script {
                    withCredentials([usernamePassword(
                      credentialsId: 'gcp-artifactory',
                      usernameVariable: 'ARTIFACTORY_USERNAME',
                      passwordVariable: 'ARTIFACTORY_PASSWORD'
                    )]) {
                            sh 'mvn --settings ./settings.xml clean install deploy -Dartifactory.username=${ARTIFACTORY_USERNAME} -Dartifactory.password=${ARTIFACTORY_PASSWORD} -Dversion=' + env.BRANCH_NAME.split("/")[1]
                        }
                }
            }
        }
    }
}
