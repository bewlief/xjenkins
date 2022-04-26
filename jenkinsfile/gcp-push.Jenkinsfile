pipeline {
    agent none
    stages {
      stage('Prepare') {
        agent {
          docker {
            image 'builder/po:v2.1.0'
            registryUrl 'https://dse-images.artifactory.gcp.anz'
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
              sh 'make build/schema-bundle'
            }
          }

          stage('Tag & Publish') {
            when {
              branch 'feature/ccd_sola_java11_jenkins'
            }
            environment{
              PATCHNUM="${currentBuild.number}"
            }
            steps {
              echo "Current PatchNumber: ${env.PATCHNUM}"
              script {
                withCredentials([usernamePassword(
                  credentialsId: 'github-app-dse',
                  usernameVariable: 'GITHUB_APP',
                  passwordVariable: 'GITHUB_TOKEN'
                )]) {
                  sh 'make tag'
                }
              }
              script {
                withCredentials([usernamePassword(
                  credentialsId: 'gcp-artifactory',
                  usernameVariable: 'ARTIFACTORY_USERNAME',
                  passwordVariable: 'ARTIFACTORY_PASSWORD'
                )]) {
                  sh 'make publish'
                }
              }
            }
          }
        }
      }
    }
}
