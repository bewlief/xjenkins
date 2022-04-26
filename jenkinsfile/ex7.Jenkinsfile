pipeline {
    agent none
    stages {
        stage('Get CPaaS Deployer Secret') {
            agent { label 'alpine-kubectl' }
            steps {
                script {
                    secretsMap = hcvault.getSecret('nonprod', 'cicd/jenkins/colabff')
                    env.CPAAS_DEPLOYER_TOKEN = secretsMap['cpaas-cola-qa-deployer-token']
                }
            }
        }
        stage('Deploy Version') {
            agent { label 'alpine-kubectl' }
            steps {
                script {
                    def userInput = input message: 'Please enter environment and version to deploy', parameters: [
            string(defaultValue: '',
              description: 'The version of the cola-bff manifest and image to deploy.',
              name: 'version', trim: true
            ),
            choice(choices: ['qa', 'pnv', 'prod'],
              description: 'The environment to deploy to.',
              name: 'environment'
            )
          ]
                    def deploy_env = userInput.environment
                    def version = userInput.version
                    echo ("environment: ${deploy_env}")
                    echo ("version: ${version}")
                    env.DEPLOY_ENV = deploy_env
                    env.DEPLOY_VERSION = version
                }
                sh 'make deploy'
            }
        }
    }
}
