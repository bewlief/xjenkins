port = new Random().nextInt(5000 - 4000) + 4000
sourceBranch = env.gitlabBranch ?: env.gitlabSourceBranch
validBranches = [
  'master',
  'develop',
  'facility-redesign-phase-ii',
  'temp-master',
]
// set name of job
currentBuild.displayName = sourceBranch
node {
  env.NODEJS_HOME = "${tool 'nodejs'}"
  // on linux / mac
  env.PATH="${env.NODEJS_HOME}/bin:${env.PATH}"
  sh 'node -v'
  sh 'npm --version'
  gitlabCommitStatus {
    stage ('Checkout') {
      checkout scm
    }
    stage ('Install') {
      sh 'npm install --no-spin'
      sh 'npm update eslint-config-an'
    }
    stage ('Lint:JS') {
      try {
        // sh 'npm run lint:checkstyle'
        sh 'npm run eslint'
      } catch(Exception e) {
        // possibly send email here, if master branch
        slackSend channel: '#anet-jenkins-test', color: '#FF0000', message: "JS linting failed on Pipeline [${currentBuild.fullDisplayName} ] \n ${env.BUILD_URL}\n "
        error 'Linting failed'
      } finally {
        // step([
        //   $class: 'CheckStylePublisher',
        //   canComputeNew: false,
        //   defaultEncoding: '',
        //   failedTotalAll: '0',
        //   healthy: '',
        //   pattern: 'eslint-checkstyle.xml',
        //   unHealthy: '0',
        //   unstableTotalAll: '0'
        // ])
      }
    }
    stage ('Lint:CSS') {
      try {
        sh 'npm run stylelint'
      } catch(Exception e) {
        // possibly send email here, if master branch
        slackSend channel: '#anet-jenkins-test', color: '#FF0000', message: "CSS linting failed on Pipeline [${currentBuild.fullDisplayName} ] \n ${env.BUILD_URL}\n "
        error 'Linting failed'
      }
    }
    stage ('Test') {
      try {
        sh "npm run test"
      } catch(Exception e) {
        // possibly send email here, if master branch
        slackSend channel: '#anet-jenkins-test', color: '#FF0000', message: "Testing failed on Pipeline [${currentBuild.fullDisplayName} ] \n ${env.BUILD_URL}\n "
        error 'Testing failed'
      } finally {
        publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: false, reportDir: 'test/coverage/', reportFiles: 'index.html', reportName: 'Test Coverage Report'])
        step([
          $class: 'CoberturaPublisher',
          autoUpdateHealth: false,
          autoUpdateStability: false,
          coberturaReportFile: 'test/coverage/cobertura-coverage.xml',
          failUnhealthy: false,
          failUnstable: false,
          maxNumberOfBuilds: 0,
          onlyStable: false,
          sourceEncoding: 'ASCII',
          zoomCoverageChart: false
        ])
      }
    }
    stage ('Prod') {
      //sh 'npm run prod'
    }
    stage('SonarQube') {
      try {
        if (sourceBranch == 'develop') {
          sh 'npm run updateSonarVersion'
          script {
            def scannerHome = tool 'scanner3.3.0';
            withSonarQubeEnv('SonarQube') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
          }
        }
      } catch(Exception ex) {
      }
    }
  }
}