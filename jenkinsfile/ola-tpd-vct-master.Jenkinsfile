pipeline {
    agent { label 'dind' }
    environment {
        KATALON_API_KEY = credentials('jenkins-katalon-api-key')
        no_proxy = "$no_proxy, *.dev.anz"
    }
    stages {
        stage('Parameters') {
            // don't ask for user input on automated builds, as there won't be anyone around to respond
            when {
                expression {
                    def cause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]
                    return cause != null
                }
            }
            steps {
                script {
                    def test_suites = sh(script: "make test-suites | sed '1,/test-suites/ d'", returnStdout: true).trim()
                    def exec_profiles = sh(script: "make exec-profiles | sed '/default/ d; 1,/exec-profiles/ d'", returnStdout: true).trim()
                    def browsers = 'Chrome\nFirefox'
                    def user_parameters = input(
                        message: 'Select a Test Suite and Browser',
                        parameters: [
                            choice(
                                name: "exec_profile",
                                choices: "${exec_profiles}",
                                description: "List of Available Execution Profiles"
                            ),
                            choice(
                                name: "test_suite",
                                choices: "${test_suites}",
                                description: "List of Available Test Suites"
                            ),
                            choice(
                                name: "browser",
                                choices: "${browsers}",
                                description: "List of Available Browser Emulations"
                            )
                        ]
                    )
                    env.KATALON_EXEC_PROFILE = user_parameters['exec_profile']
                    env.KATALON_TEST_SUITE_FILE = user_parameters['test_suite']
                    env.KATALON_BROWSER = user_parameters['browser']
                }
            }
        }
        stage ('Run Tests') {
            steps {
                sh 'make'
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
