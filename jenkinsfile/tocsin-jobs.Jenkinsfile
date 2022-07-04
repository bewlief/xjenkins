// https://github.service.xhoe/dsso/tocsin-jobs/blob/master/Jenkinsfile
pipeline {
  agent none
  stages {
    stage('Build') {
      agent { label 'nodejs-14' }
      when { branch 'master' }
      steps {
        script {
          sh 'npm ci'
          sh 'npm run build'

          dir("dist") {
            artifactory.publish("tocsin-jobs/", "./", [
              recursive: true,
              flat: false
            ])
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
