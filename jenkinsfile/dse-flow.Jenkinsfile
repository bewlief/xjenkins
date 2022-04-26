pipeline {
  agent none
  stages {

    stage('Integration') {
      agent { label 'alpine' }
      when {
        branch 'PR-*'
        beforeAgent true
      }
      stages {
        stage('Test') {
          steps {
            script { STAGE_NAME = env.STAGE_NAME }
            echo env.STAGE_NAME
          }
        }
      }
    }

    stage('Build & Publish') {
      agent { label 'alpine' }
      when {
        branch 'develop'
        beforeAgent true
      }
      stages {
        stage('Build') {
          steps {
            script { STAGE_NAME = env.STAGE_NAME }
            echo env.STAGE_NAME
          }
        }
        stage('Publish') {
          steps {
            script { STAGE_NAME = env.STAGE_NAME }
            echo env.STAGE_NAME
          }
        }
      }
    }

    stage('Deployment [develop]') {
      agent { label 'alpine' }
      when {
        branch 'develop'
        beforeAgent true
      }
      options { lock 'apply-integration-environment' }
      stages {
        stage('Deploy: SIT3') {
          steps {
            script { STAGE_NAME = env.STAGE_NAME }
            echo env.STAGE_NAME
          }
        }
        stage('E2E Tests') {
          steps {
            script { STAGE_NAME = env.STAGE_NAME }
            echo env.STAGE_NAME
          }
        }
        stage('Merge to master') {
          steps {
            milestone 1
            script {
              STAGE_NAME = env.STAGE_NAME
              echo env.STAGE_NAME
              ghe '''
                git checkout -B master
                git rebase origin/develop
                git push origin master
              '''
            }
          }
        }
      }
    }

    stage('Deployment [master]') {
      when {
        branch 'master'
        beforeAgent true
      }
      stages {
        stage('Tag release') {
          steps {
            script { STAGE_NAME = env.STAGE_NAME }

            echo env.STAGE_NAME
          }
        }
        stage('Deploy: Pre-production') {
          steps {
            milestone 2
            script { STAGE_NAME = env.STAGE_NAME }

            echo env.STAGE_NAME
          }
        }
        stage('Deploy: Production') {
          steps {
            milestone 3
            script { STAGE_NAME = env.STAGE_NAME }

            echo "${env.STAGE_NAME}: input"
            input "Deploy to production"
            echo "${env.STAGE_NAME}: approved"
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
