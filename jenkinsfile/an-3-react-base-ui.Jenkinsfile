port = new Random().nextInt(5000 - 4000) + 4000
sourceBranch = env.gitlabSourceBranch ?: env.sourceBranch
targetBranch = env.gitlabTargetBranch ?: env.targetBranch
validBranches = [
  'master',
  'CUI'
]
// set name of job
currentBuild.displayName = sourceBranch + ' => ' + targetBranch
node {
  gitlabCommitStatus {
    stage ('Checkout') {
      // echo sh(returnStdout: true, script: 'env')
      
      echo 'sourceBranch:' + sourceBranch
      echo 'targetBranch:' + targetBranch
      
      sh 'pwd'
      sh 'rm -rf ./*'
      sh 'node -v'
      checkout changelog: true, poll: true, scm: [
        $class: 'GitSCM',
        branches: [[name: "origin/${sourceBranch}"]],
        doGenerateSubmoduleConfigurations: false,
        credentialsId: '2a26b18c-41f7-4b9f-b101-5482c89b0431',
        extensions: [
          [
            $class: 'PreBuildMerge', 
            options: [
              fastForwardMode: 'FF', 
              mergeRemote: 'origin', 
              mergeStrategy: 'DEFAULT', 
              mergeTarget: "${targetBranch}"
            ]
          ]
        ],
        submoduleCfg: [],
        userRemoteConfigs: [
          [name: 'origin', url: 'git@gitlab.dev.activenetwork.com:ActiveNet/react-base-ui.git']
        ]
      ]
    }
    stage ('Install') {
      sh 'node -v'
      sh 'npm -version'
      sh 'npm install yarn'
      sh 'yarn install'
      // sh 'npm install --no-spin'
    }
     stage ('Lint:JS') {
      try {
        // sh 'npm run lint:checkstyle'
        if (targetBranch == 'master') {
          sh 'yarn eslint'
        } else {
          sh 'yarn lint'
        }
      } catch(Exception e) {
        // possibly send email here, if master branch
        error 'Linting JS failed'
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
      if (targetBranch == 'master') {
        try {
          sh 'yarn stylelint'
        } catch(Exception e) {
          // possibly send email here, if master branch
          error 'Linting CSS failed'
        }
      }
    }
    stage ('Test') {
      try {
        sh "yarn test"
      } catch(Exception e) {
        error 'Testing failed'
      } finally {
        publishHTML([
          allowMissing: true, 
          alwaysLinkToLastBuild: true, 
          keepAll: false, 
          reportDir: 'test/coverage/', 
          reportFiles: 'index.html', 
          reportName: 'Test Coverage Report'
        ])
          
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
    stage('SonarQube') {
      if (targetBranch == 'master' && sourceBranch == 'master') {
        try {
          sh "yarn updateSonarVersion"
          script {
            def scannerHome = tool 'scanner3.3.0';
            withSonarQubeEnv('SonarQube') {
              sh "${scannerHome}/bin/sonar-scanner -X"
            }
          }
        } catch(Exception ex) {
        }
      }
    }

    stage ('Examples') {
      try {
        sh "yarn site"
      } catch(Exception e) {
        error 'Launching examples failed'
      }
    }
  }
}
