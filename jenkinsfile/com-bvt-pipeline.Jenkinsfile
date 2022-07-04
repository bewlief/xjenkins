pipeline {
  agent {
    kubernetes {
      label "k8s-ola-kalaton"
      inheritFrom 'alpine'
      cloud  "openshift"
      namespace "XOPS-jenkins"
      yaml """
spec:
  securityContext:
    runAsUser: 0
  containers:
  - name: katalon
    image: hub.artifactory.gcp.xhoe/katalonstudio/katalon:7.7.2
    command:
    - cat
    tty: true
"""
    }
  }

  options {
    timeout(time: 2, unit: 'HOURS')
    disableConcurrentBuilds()
    buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '7', numToKeepStr: '50')
  }

  parameters {
    string defaultValue: 'master', description: 'git branch', name: 'BRANCH', trim: true
    choice choices: ['EVE','WALLE','VisualTest','Luna','Flash'], description: 'project name', name: 'PROJECT'
    choice choices: ['Chrome', 'Firefox'], description: '', name: 'BROWSER'
    choice choices: ['default' ,'test1', 'test2','test3', 'test4', 'prod','convm'], description: '', name: 'EXECUTION_PROFILE'
    string defaultValue: 'Test Suites', description: 'the path of test suite', name: 'TEST_SUITE_COLLECTION_PATH', trim: true
    password defaultValue: '', description: 'api key to activate license ', name: 'API_KEY'
    string defaultValue: '74580', description: '', name: 'ORG_ID', trim: true
  }

  stages {

    stage ('checkout test case') {
      steps {
        container('jnlp') {
          checkout([$class: 'GitSCM', branches: [[name: "*/${BRANCH}"]],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github-app-XOPS',
                                                           url: 'https://github.service.xhoe/dsso/xhoecom-test-automation.git']]])
          sh '''set +x
            echo 'show folder content'
            ls -al
          '''
        }
      }
    }

    stage("run katalon") {
      steps {
        container('katalon') {
          dir ("Katalon/${PROJECT}") {
            sh '''set +x
                proxy_fqdn=${https_proxy#*//}
                proxy_port=${proxy_fqdn#*:}
                proxy_fqdn=${proxy_fqdn%:*}
                sed -i '3s/-x/+x/g' /katalon/scripts/katalonc.sh
                katalonc.sh -projectPath=$PWD -browserType=${BROWSER} -retry=0 \
                -statusDelay=15 \
                -testSuiteCollectionPath="${TEST_SUITE_COLLECTION_PATH}" \
                -executionProfile=${EXECUTION_PROFILE} \
                -apikey=${API_KEY} \
                -orgID=${ORG_ID} \
                --config \
                -proxy.auth.option=MANUAL_CONFIG \
                -proxy.auth.server.type=HTTPS \
                -proxy.auth.server.address=${proxy_fqdn} \
                -proxy.auth.server.port=${proxy_port} \
                -proxy.auth.excludes="${no_proxy},.dev.xhoe" \
                -proxy.system.option=MANUAL_CONFIG \
                -proxy.system.server.type=HTTPS \
                -proxy.system.server.address=${proxy_fqdn} \
                -proxy.system.server.port=${proxy_port} \
                -proxy.system.excludes="${no_proxy},.dev.xhoe" \
                -proxy.system.applyToDesiredCapabilities=true
            '''
          }
        }
      }
    }
  }
  post{
    always{
      dir ("Katalon/${PROJECT}") {
        archiveArtifacts allowEmptyArchive: true, artifacts: 'Reports/**/*.*'
        junit allowEmptyResults: true, testResults: 'Reports/**/JUnit_Report.xml'
      }
    }
  }
}
