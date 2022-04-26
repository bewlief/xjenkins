@Library('dse-jobs@feature/update-artefact-publish') _
pipeline {
    agent {
        label 'nodejs-8-build-utils'
    }
    environment {
        appName = 'sbola-ui'
        component = 'ui'
        STAGE_NAME = ''
    }
	stages {
		stage('Checkmarx Code Scan') {
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
	                        "!**/.git/**/*",
	                        "!Jenkinsfile",
	                        "!Makefile",
	                        "!Gruntfile.js",
	                        "!.gitignore",
	                        "!.nvmrc",
	                        "!.npmrc",
	                        "!mocha.opts",
	                        "!package.json",

	                        // Local development files
	                        "!scripts/**/*",
	                        "!config/**/*",
	                        "!e2e/**/*",
	                        "!app/src/assets/**/*",
	                        "!app/src/**/**/*.spec.js*",
	                        "!app/src/components/common/**/*.spec.js*",
	                        "!app/src/**/*.spec.js*",
	                        "!**/.storybook/*",
	                    ]
		            ])
		        }
		    }
		}
		stage('Prepare') {
            steps {
                script {
                    STAGE_NAME = env.STAGE_NAME
                }
                sh 'make prepare'
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
                    HASH = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    BRANCH = sh(returnStdout: true, script: "git name-rev --name-only HEAD | sed 's|remotes/origin/release/||' | sed 's|remotes/origin/feature/||' | sed 's|remotes/origin/bugfix/||' | sed 's|remotes/origin/||'").trim()
                    artifactory.publish("${component}/${appName}/${BRANCH}-${HASH}/", "*.zip", [
                            repository: "omni-generic-releases"
                    ])
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
