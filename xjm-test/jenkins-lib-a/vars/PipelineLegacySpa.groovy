
def call(appName, component, opt = [:]) {

  def conf = [label: 'nodejs-8-build-utils'] << opt
  def STAGE_NAME = "";

  pipeline {
    agent none
    stages {
      stage('Prepare') {
        agent { label conf.label }
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
                version = sh(returnStdout: true, script: 'git describe --tags').trim()
                artifactory.publish("${component}/${appName}/${version}/", "*.zip", [
                   repository: "omni-generic-releases"
                ])
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
}
