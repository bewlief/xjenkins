pipeline {
  agent none
  stages {
     stage('Agent-build_docker') {
        agent { label 'dind' }
        stages{
            stage('Build docker'){
                steps{
                    sh './scripts/version-and-tag.sh'
                    sh 'make build/docker image=tpdservice'
                }
            }
            stage('Push') {
                steps {
                    script {
                        tag = sh(returnStdout: true, script: 'make tag image=tpdservice').trim()
                        artifactory.dockerPush(tag)
                    }
                }
            }
        }
     }
  }
}