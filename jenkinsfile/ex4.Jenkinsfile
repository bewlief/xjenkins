pipeline {
    agent 'none'
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build and Test') {
            agent { label 'dind' }
            steps {
                script {
                    secretsMap = hcvault.getSecret('nonprod', 'apply-tools-os-deployer-token')
                    env.oc_registry_token = secretsMap['token']
                }
                dir ('cb-ghe-slack-notifier') {
                    sh 'make build-ci'
                }
            }
        }
        stage('Manual Deploy Trigger') {
            when { branch 'master' }
            steps {
                input 'Deploy?'
            }
        }
        stage('Deploy') {
            when { branch 'master' }
            agent { label 'dind' }
            steps {
                dir ('cb-ghe-slack-notifier') {
                    sh 'make ci'
                }
            }
        }
    }
}
