pipeline {
  agent {
    kubernetes {
      label "k8s-ola-kalaton"
      inheritFrom 'alpine'
      cloud  "openshift"
      namespace "dse-jenkins"
      yaml """
spec:
  securityContext:
    runAsUser: 0
  containers:
  - name: katalon
    image: hub.artifactory.gcp.anz/katalonstudio/katalon:7.7.2
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
    string defaultValue: 'Test Suites', description: 'the path of test suite', name: 'TEST_SUITE_PATH', trim: true
    choice choices: ['Chrome', 'Firefox'], description: '', name: 'BROWSER'
    choice choices: ['default' ,'test1', 'test2','test3', 'test4', 'prod','convm'], description: '', name: 'EXECUTION_PROFILE'
    password defaultValue: '', description: 'api key to activate license ', name: 'API_KEY'
    string defaultValue: '74580', description: '', name: 'ORG_ID', trim: true
  }

  stages {

    stage ('checkout test case') {
      steps {
        container('jnlp') {
          checkout([$class: 'GitSCM', branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github-app-dse',
                                                           url: 'https://github.service.anz/dsso/TA-TPD-Katalon.git']]])
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
          sh '''set +x
            proxy_fqdn=${https_proxy#*//}
            proxy_port=${proxy_fqdn#*:}
            proxy_fqdn=${proxy_fqdn%:*}
            sed -i '3s/-x/+x/g' /katalon/scripts/katalonc.sh
            katalonc.sh -projectPath=$PWD -browserType=${BROWSER} -retry=0 \
              -statusDelay=15 \
              -testSuitePath="${TEST_SUITE_PATH}" \
              -executionProfile=${EXECUTION_PROFILE} \
              -apikey=${API_KEY} \
              -orgID=${ORG_ID} \
              --securitydemo.config \
              -aopdemo.proxy.auth.option=MANUAL_CONFIG \
              -aopdemo.proxy.auth.server.type=HTTPS \
              -aopdemo.proxy.auth.server.address=${proxy_fqdn} \
              -aopdemo.proxy.auth.server.port=${proxy_port} \
              -aopdemo.proxy.auth.excludes="${no_proxy},.dev.anz" \
              -aopdemo.proxy.system.option=MANUAL_CONFIG \
              -aopdemo.proxy.system.server.type=HTTPS \
              -aopdemo.proxy.system.server.address=${proxy_fqdn} \
              -aopdemo.proxy.system.server.port=${proxy_port} \
              -aopdemo.proxy.system.excludes="${no_proxy},.dev.anz" \
              -aopdemo.proxy.system.applyToDesiredCapabilities=true
          '''
        }
      }
    }
  }
  post{
    always{
      archiveArtifacts allowEmptyArchive: true, artifacts: 'Reports/**/*.*'
      junit allowEmptyResults: true, testResults: 'Reports/**/JUnit_Report.xml'
    }
  }
}