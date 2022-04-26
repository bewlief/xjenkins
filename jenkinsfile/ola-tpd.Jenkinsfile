// https://dse-jenkins-ci.apps.omni.service.test/job/technology/job/ola-tpd/job/ui/job/feature%252FOLA-3575-Remove-Roles
def opt = [:]
def STAGE_NAME = "";
pipeline {
  agent none
  stages {
    stage('Prepare') {
      agent { label 'nodejs-8-build-utils' }
      stages {
        stage('Test') {
          steps {
            script {
              STAGE_NAME = env.STAGE_NAME
            }
            sh 'make prepare check test'
          }
        }
        stage('Build') {
          steps {
            script {
              STAGE_NAME = env.STAGE_NAME
            }
            sh 'make build'
          }
        }
        stage('Publish') {
          steps {
            script {
              STAGE_NAME = env.STAGE_NAME
              // Version in the artifact path is required to support UDeploy.
              version = sh(returnStdout: true, script: 'git describe --tags').trim()
              artifactory.publish("${"tpd"}/${"ui"}/${version}/", "*.zip")
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



