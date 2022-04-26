pipeline {
  agent any
  environment {
    buildDir = 'checkout-cui'
    repoSSHURL = 'git@gitlab.dev.activenetwork.com:PlatformServices/checkout-cui.git'
    gw = './gradlew -Dorg.gradle.java.home=/usr/java/default'
    SONAR = credentials('54e7c18e-da9d-40a1-b647-08a866296090')
  }
  options {
    skipDefaultCheckout()
    gitLabConnection('Gitlab')
    gitlabBuilds(builds: ["Compile", "Test", "Inspect"])
  }
  triggers {
    gitlab(triggerOnPush: true,
           triggerOnMergeRequest: true,
           skipWorkInProgressMergeRequest: true,
           branchFilterType: 'All',
           secretToken: 'jenkinswebhook')
  }
  
  post {
    always {
      deleteDir()
    }
  }
  stages {
    stage("Checkout") {
      steps {
        script {
          if (env.gitlabMergeRequestId) {
            echo 'Gitlab merge request triggered build'
            checkout ([
              $class: 'GitSCM',
              branches: [[name: "origin/${env.gitlabSourceBranch}"]],
              extensions: [
                [$class: 'RelativeTargetDirectory', relativeTargetDir: "${buildDir}"],
                [$class: 'LocalBranch'],
                [$class: 'PreBuildMerge', options: [
                  fastForwardMode: 'FF',
                  mergeRemote: 'origin',
                  mergeStrategy: 'DEFAULT', 
                  mergeTarget: env.gitlabTargetBranch
                ]]
              ],
              userRemoteConfigs: [[url: "${repoSSHURL}"]]
            ])
          } else {
            echo 'Jenkins user triggered build'
            def sourceBranch = env.gitlabBranch ?: params.gitlabBranch
            checkout ([
              $class: 'GitSCM',
              branches: [[name: "origin/${sourceBranch}"]], 
              extensions: [
                [$class: 'RelativeTargetDirectory', relativeTargetDir: "${buildDir}"],
                [$class: 'LocalBranch']
              ],
              userRemoteConfigs: [[url: "${repoSSHURL}"]]
            ])
          }
        }
      }
    }
    /**
    stage("Fortify Remote Analysis") {
      steps {
        script {
          def props = readProperties file: "${env.WORKSPACE}/${buildDir}/version.properties";
          env['VERSION'] = props['version']
        }
        fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyGradle(),
          uploadSSC: [appName: '${buildDir}', appVersion: '${VERSION}']
      }
    }
    **/
    stage("Init") {
      steps {
        sh "cd ${buildDir}; ${gw} ciInit"
        script {
          def props = readProperties file: "${buildDir}/version.properties";
          currentBuild.displayName = props['version']
          sh 'if [ ! -d  "/opt/active/user/deploy/jobs/checkout-cui-promotion/workspace" ] ; then mkdir /opt/active/user/deploy/jobs/checkout-cui-promotion/workspace ; fi'
          sh " cp ${buildDir}/version.properties /opt/active/user/deploy/jobs/checkout-cui-promotion/workspace/"
        }
      }
    }
    stage("Compile") {
      steps {
        gitlabCommitStatus("Compile") {
          sh "cd ${buildDir}; ${gw} ciCompile"
        }
      }
    }
    
    stage("Test") {
      steps {
        gitlabCommitStatus("Test") {
          sh "cd ${buildDir}; ${gw} ciTest"
          junit '**/build/test-results/**/TEST-*.xml'
        }
      }
    }
    stage("Inspect") {
      steps {
        gitlabCommitStatus("Inspect") {
          sh "cd ${buildDir}; ${gw} ciInspect"
        }
      }
    }
    /*stage("PublishApiDoc") {
      when { environment name: 'gitlabMergeRequestId', value: '' }
      steps {
        sh "cd ${buildDir}; ${gw} ciPublishApiDoc"
      }
    } */
    stage("Publish") {
      when { environment name: 'gitlabMergeRequestId', value: '' }
      steps {
        sh "cd ${buildDir}; ${gw} ciPublish -Dsonar.login=${SONAR}"
        build job: 'checkout-cui-promotion' 
      }
    }
    stage("BlackDuck Scan") {
      steps {
        synopsys_detect detectProperties: "--detect.test.connection=false --detect.project.name=AN_${buildDir} --detect.force.success=true --detect.tools=ALL --detect.blackduck.signature.scanner.individual.file.matching=ALL", returnStatus: true  
      }
    }
    stage("Security Scan") {
      steps {
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
        sh "zip -r fod.zip checkout-cui/build/libs checkout-cui/api/src/main"
        sh "java -jar /opt/active/user/deploy/FodUpload.jar -z fod.zip -ac 6a9e5337-83e8-474f-8fbc-e2dbaa01c78b '2iX02MZ;TjrU0i3o%jZck]VzENYrXY' -bsi eyJ0ZW5hbnRJZCI6MTY1MiwidGVuYW50Q29kZSI6IkdQQU4iLCJyZWxlYXNlSWQiOjE4MzE0NiwicGF5bG9hZFR5cGUiOiJBTkFMWVNJU19QQVlMT0FEIiwiYXNzZXNzbWVudFR5cGVJZCI6Mjc0LCJ0ZWNobm9sb2d5VHlwZSI6IkpBVkEvSjJFRSIsInRlY2hub2xvZ3lUeXBlSWQiOjcsInRlY2hub2xvZ3lWZXJzaW9uIjoiMS44IiwidGVjaG5vbG9neVZlcnNpb25JZCI6MTIsImF1ZGl0UHJlZmVyZW5jZSI6IkF1dG9tYXRlZCIsImF1ZGl0UHJlZmVyZW5jZUlkIjoyLCJpbmNsdWRlVGhpcmRQYXJ0eSI6ZmFsc2UsImluY2x1ZGVPcGVuU291cmNlQW5hbHlzaXMiOmZhbHNlLCJwb3J0YWxVcmkiOiJodHRwczovL2Ftcy5mb3J0aWZ5LmNvbSIsImFwaVVyaSI6Imh0dHBzOi8vYXBpLmFtcy5mb3J0aWZ5LmNvbSIsInNjYW5QcmVmZXJlbmNlIjoiU3RhbmRhcmQiLCJzY2FuUHJlZmVyZW5jZUlkIjoxfQ== -ep 2"
      }
    }
    }
  }
}
