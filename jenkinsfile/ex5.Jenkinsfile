pipeline {
    agent none
    environment {
        appName = 'cola-ui'
        component = 'ui'
        STAGE_NAME = ''
    }
    stages {
        stage('Checkmarx Code Scan') {
            agent {
                label 'nodejs'
            }
            when {
                anyOf {
                    branch 'release/*'
                }
            }
            steps {
                script {
                    checkmarx.scan([
            // extend parameter in dsso/jenkins-lib/var/checkmarx.groovy
            filters: [
              // Configuration files
              '!**/.git/**/*',
              '!Jenkinsfile',
              '!Makefile',
              '!Dockerfile',
              '!.babelrc',
              '!.eslintignore',
              '!.eslintrc.json',
              '!.gitignore',
              '!.istanbul.yml',
              '!.java-version',
              '!.nvmrc',
              '!.yarnrc',
              '!CODEOWNERS',
              '!docker-compose.yml',
              '!package.json',
              '!test-report.xml',
              '!webpack*',
              '!yarn*',

              // Local development files
              '!.devcontainer/**/*',
              '!.storybook/**/*',
              '!.vscode/**/*',
              '!__mocks__/**/*',
              '!app/e2e/**/*',
              '!app/config/**/*',
              '!app/src/**/*.spec.js*',
              '!app/src/**/*.test.js*',
              '!app/src/**/*.stories.js*',
              '!cache/**/*',
              '!config/**/*',
              '!npm-packages-offline-cache/**/*',
              '!scripts/**/*',
              '!selenium/**/*',
              '!tools/**/*',
            ]
          ])
                }
            }
        }
        stage('Prepare') {
            agent {
                label 'nodejs-8-build-utils'
            }
            stages {
                stage('Test') {
                    steps {
                        script {
                            STAGE_NAME = env.STAGE_NAME
                        }
                        sh 'make prepare check test'
                    }
                }
                stage('Build') {
                    steps {
                        script {
                            STAGE_NAME = env.STAGE_NAME
                        }
                        sh 'make build'
                    }
                }
                stage('Publish') {
                    steps {
                        script {
                            STAGE_NAME = env.STAGE_NAME
                            version = sh(returnStdout: true, script: 'git describe --tags --long').trim()
                            artifactory.publish("${component}/${appName}/${version}/", '*.zip', [
                 repository: 'omni-generic-releases'
              ])
                        }
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
