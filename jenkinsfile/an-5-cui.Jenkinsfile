sourceBranch = env.gitlabBranch ?: env.gitlabSourceBranch
// set name of job
currentBuild.displayName = sourceBranch
node {
  gitlabCommitStatus {
    stage ('Checkout') {
      sh 'node --version'
      sh 'npm --version'
      sh 'npm config list'
      checkout scm
    }
    stage ('Install Package') {
      sh 'npm install --no-spin'
    }
    stage ('Build Prod') {
      sh 'npm run build:i18n && npm run build:prod'
    }
    stage ('Lint:JS') {
      try {
        sh 'npm run eslint:checkstyle'
      } catch(Exception e) {
        // possibly send email here, if master branch
        slackSend channel: '#anet-cui-jenkins', color: '#FF0000', message: "Linting js failed on Pipeline [${currentBuild.fullDisplayName} ] \n ${env.BUILD_URL}\n "
        error 'Linting failed'
      } finally {
        step([
          $class: 'CheckStylePublisher',
          canComputeNew: false,
          defaultEncoding: '',
          failedTotalAll: '1',
          healthy: '',
          pattern: 'eslint-checkstyle.xml',
          unHealthy: '1'
        ])
      }
    }
    stage ('Lint:CSS') {
      try {
        sh 'npm run stylelint'
      } catch(Exception e) {
        // possibly send email here, if master branch
        slackSend channel: '#anet-cui-jenkins', color: '#FF0000', message: "Linting css failed on Pipeline [${currentBuild.fullDisplayName} ] \n ${env.BUILD_URL}\n "
        error 'Linting failed'
      }
    }
    stage ('Test') {
      try {
        sh "npm run test:autoport"
      } catch(Exception e) {
        currentBuild.result = "FAILURE"
        error 'Testing failed'
        slackSend channel: '#anet-cui-jenkins', color: '#FF0000', message: "Testing failed on Pipeline [${currentBuild.fullDisplayName} ] \n ${env.BUILD_URL}\n "
        throw err
      } // finally {
        // publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'test/coverage/', reportFiles: 'index.html', reportName: 'Test Coverage Report'])
      // }
    }
    stage('SonarQube') {
      if (sourceBranch == 'develop') {
        try {
          sh 'npm run updateSonarVersion'
          script {
            def scannerHome = tool 'scanner3.3.0';
            withSonarQubeEnv('SonarQube') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
           }
        } catch(Exception ex) {
        }
      }
    }
    
  }
}
