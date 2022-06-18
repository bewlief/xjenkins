pipeline {
  agent none
  stages {
    stage('Prepare') {

      agent {
        docker {
          image 'builder/po:v2.0.2'
          registryUrl 'https://xops-images.artifactory.gcp.xhoe'
        }
      }

      stages {
        stage('Prepare PO') {
          steps {
            sh 'make prepare'
          }
        }
        stage('Build') {
          steps {
            sh 'make build/plugin'
          }
        }
        stage('Publish') {
          steps {
            script {
              // TODO: Publish
              withCredentials([usernamePassword(
                credentialsId: 'gcp-artifactory',
                usernameVariable: 'ARTIFACTORY_USERNAME',
                passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
                sh 'make publish'
              }
            }
          }
        }
      }
    }
  }
}
