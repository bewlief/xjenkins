@Library("XOPS-jobs") _

pipeline {
    agent any

    stages {
        stage("checkout codes") {
            steps {
                script {
                    echo "checkout codes"
                }
            }
        }
        stage("static check") {
            parallel {
                stage("mvn checkstyle") {
                    steps {
                        script {
                            echo "mvn checkstyle"
                            scan.mvnCheckstyle()
                        }
                    }
                }

                stage("mvn sonar") {
                    steps {
                        script {
                            echo "mvn sonar"
                        }
                    }
                }

                stage("checkmax") {
                    steps {
                        script {
                            echo "checkmax, or a single job for it?"
                        }
                    }
                }
            }
        }
        stage("sync to AMS git") {
            steps {
                script {
                    echo "sync codes into AMS"
                }
            }
        }

        stage("change AMS pipeline") {
            steps {
                script {
                    echo "set to correct dev env"
                }
            }
        }

        stage("deploy to dev env") {
            steps {
                script {
                    echo " trigger AMS pipeline automatically"
                }
            }
        }

    }

    post {
        always {
            echo "always show these info"
            echo currentBuild.currentResult
            echo "${env.JOB_NAME}, ${env.BUILD_NUMBER}"
        }
    }
}
