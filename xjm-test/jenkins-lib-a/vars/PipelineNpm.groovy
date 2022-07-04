
def call() {
  pipeline {
    agent none
    stages {
      stage('Prepare') {
        agent { label 'nodejs' }
        stages {
          stage('Test') {
            steps {
              sh 'make prepare check test'
            }
          }
          stage('Build') {
            steps {
              sh 'make build'
            }
          }
          stage('Publish') {
            when { branch 'master' }
            steps {
              script {
                npm.publish()
              }
            }
          }
        }
      }
    }
    post {
      failure {
        script {
          slack.failedBuild()
        }
      }
    }
  }
}
