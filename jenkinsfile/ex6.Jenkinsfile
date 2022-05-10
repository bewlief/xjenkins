def app_build_version = 'default'
pipeline {
  agent none
  environment {
      PATCHNUM="${currentBuild.number}"
      APP_NAME = 'cola-bff'
      STAGE_NAME = ''
      BUILD_VERSION = ''
  }
  stages {
    stage('Prepare') {
      agent { label 'dind' }
      stages {
        stage('Version') {
          steps {
            echo "current build number: ${currentBuild.number}"
            echo "Env PATCHNUM: ${env.PATCHNUM}"
            echo "Current Jenkins Pipeline Name: ${env.JOB_BASE_NAME}"
            script {
              STAGE_NAME = env.STAGE_NAME

              withCredentials([usernamePassword(
              credentialsId: 'github-app-XOPS',
              usernameVariable: 'GITHUB_APP',
              passwordVariable: 'GITHUB_TOKEN')]) {
                sh 'make version'
              }
            }
          }
        }

        stage('Build') {
            steps {
                script {
                  STAGE_NAME = env.STAGE_NAME
                  BUILD_VERSION = sh(returnStdout: true, script: 'make build-version').trim()
                  app_build_version = BUILD_VERSION
                  echo "BUILD_VERSION (build) : ${BUILD_VERSION}"
                  echo "app_build_version (build) : ${app_build_version}"
                }
                sh 'ls -la'
                sh 'make build'
            }
        }

        stage('Get DTR Deployer Secret') {
            steps {
              script {
                secretsMap = hcvault.getSecret('nonprod','cicd/jenkins/dtr')
                env.DTR_USERNAME = secretsMap['username']
                env.DTR_PASSWORD = secretsMap['password']
              }
            }
        }

        stage('Push') {
            when {
                anyOf {
                  branch 'develop';
                  branch 'release/*'
                }
            }
           steps {
               script {
                   STAGE_NAME = env.STAGE_NAME
                   tag = sh(returnStdout: true, script: 'make tag').trim()
                   //artifactory.dockerPush(Push tag as Variable)
                   echo "Current Jenkins Pipeline Name: ${env.JOB_BASE_NAME}"
                   withCredentials([usernamePassword(
                                 credentialsId: 'gcp-artifactory-docker',
                                 usernameVariable: 'username',
                                 passwordVariable: 'password')]) {
                                   sh 'make docker-push'
                                 }
               }
           }
        }
        stage('Generate & Publish Manifests') {
           agent { label 'alpine-kubectl' }
           when {
               anyOf {
                 branch 'develop';
                 branch 'release/*'
               }
           }
           steps {
               script {
                    STAGE_NAME = env.STAGE_NAME

                    echo "BUILD_VERSION: ${BUILD_VERSION}"
                    echo "app_build_version: ${app_build_version}"

                    sh "make generate-manifests BUILD_VERSION=${BUILD_VERSION}"


                    if (env.BRANCH_NAME == 'develop') {
                        artifactory.publish("${APP_NAME}/${BUILD_VERSION}/", "deployment-manifests.zip", [
                            repository: "XOPS-generic-snapshots"])
                    } else {
                        artifactory.publish("${APP_NAME}/${BUILD_VERSION}/", "deployment-manifests.zip", [
                            repository: "XOPS-generic-releases"])
                    }
               }
           }
        }
      }
    }
  }

  post {
    failure {
      script {
        slack.failedBuild([stageName: STAGE_NAME])
      }
    }
    fixed {
      script {
        slack.fixedBuild()
      }
    }
  }
}
