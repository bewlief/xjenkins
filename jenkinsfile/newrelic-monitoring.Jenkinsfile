// https://github.service.xhoe/dsso/newrelic-monitoring/blob/feature/SERS-3682/Jenkinsfile
pipeline {
  agent { label 'terraform-0.12.13' }
  environment {
    no_proxy = "$no_proxy"
    HTTPS_PROXY = "$HTTPS_PROXY"
    TF_STATE_REPO = 'git@github.service.xhoe:dsso/newrelic-monitoring-states.git'
    TF_STATE_DIR = '/tmp/tfstate'
    TF_CHANGE_PLAN = '/tmp/terraform-changes.plan'
  }
  stages {
    stage ('Get Secrets') {
      steps {
        script {
          secretsMap = hcvault.getSecret('nonprod','cicd/newrelic')
          env.TF_VAR_newrelic_api_key = secretsMap['api_key']
        }
      }
    }
    stage ('Synch. Terraform State') {
      steps {
        checkout (
          changelog: false,
          poll: false,
          scm: [$class: 'GitSCM',
              branches: [[name: 'master']],
              doGenerateSubmoduleConfigurations: false,
              extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: env.TF_STATE_DIR]],
              submoduleCfg: [],
              userRemoteConfigs: [[
                  credentialsId: 'XOPS-jenkins-ssh',
                  url: env.TF_STATE_REPO
              ]]
          ]
        )
      }
    }
    stage ('Plan') {
      steps {
        sh 'make plan'
        script {
          deployStatus = sh(script: "[ -f $TF_CHANGE_PLAN ] && echo Yes || echo No", returnStdout: true).trim()
        }
      }
    }
    stage('Deploy/Apply') {
      when {
        allOf {
          branch 'master'
          expression { deployStatus == 'Yes' }
        }
      }
      steps {
        script {
          input (message: 'Deploy Changes ?')
        }
        sh 'make apply'
      }
    }
  }
}
