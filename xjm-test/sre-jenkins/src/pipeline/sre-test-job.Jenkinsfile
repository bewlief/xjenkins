#!groovy
package pipeline

@Library('jenkinslib@dev') _

def tools = new com.xhoe.ops.tools()
def email = new com.xhoe.ops.toemail()

pipeline {
    agent any

    environment {
        CRED_ID = '*****-****-****-****-*********'
        QA_EMAIL = '*****@*****.com'
        ITEST_JOBNAME = 'x1'

    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }

    parameters {
        choice(
                choices: 'dev\nuat\nprod',
                description: 'Choose deploy environment',
                name: 'deploy_env'
        )
        string(
                name: 'app_repo',
                defaultValue: 'https://gitee.com/bewlief/sre-app.git',
                description: 'Git repo of your application'
        )
        string(
                name: 'app_branch',
                defaultValue: 'dev',
                description: 'Branch of you application'
        )
    }

    post {
        success {
            echo "successfullu build"
        }
        failure {
            echo "FAILURE"

        }
        unstable {
            echo "UNSTABLE"
        }
    }

    stages {
        stage("just test") {
            steps {
                script {
                    println("get code from git now ")
                    echo "${CRED_ID}"
                    tools.message("get code", "green")
                }
            }
        }

        stage('checkout source codes') {
            steps {
                echo "git checkout codes from $app_repo: $app_branch, deploy to $deploy_env "
                checkout([
                        $class                           : 'GitSCM',
                        branches                         : [[name: "*/$app_branch"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [[$class: 'RelativeTargetDirectory', relativeTargetDir: './sre-app']],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [
                                [credentialsId: '7c722d23-018f-4a64-9764-9161aa85681e', url: "$app_repo"]
                        ]
                ])
                echo "TODO: retrieve application name/version from POM"
                sh '''
                    set +x
                    cd ./sre-app/
                    pwd
                    $MAVEN_HOME/bin/mvn clean package -Dmaven.test.skip=true
                '''
            }
        }

        stage("static check") {
            when {
                not {
                    branch "master"
                }
            }
            failFast true
            parallel {
                stage("SpotBugs") {
                    steps {
                        echo "mvn SpotBugs check"
                    }
                }

                stage("check-style") {
                    steps {
                        echo "mvn check style"
                    }
                }

                stage("SAST") {
                    steps {
                        echo "SAST"
                    }
                }
            }
        }

        stage("build image") {
            steps {
                sh '''
                    mkdir -p /tmp/ops
                '''
                checkout([
                        $class                           : 'GitSCM',
                        branches                         : [[name: 'master']],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [[$class: 'RelativeTargetDirectory', relativeTargetDir: './ops']],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [
                                [credentialsId: '7c722d23-018f-4a64-9764-9161aa85681e', url: 'https://gitee.com/bewlief/sre-ops.git']
                        ]
                ])

                dir("${env.WORKSPACE}") {
                    sh '''
                        set +x
                        echo "copy jar to /tmp/ops/terraform/"
                        cp ./sre-app/target/sre-test-foo-0.0.1-SNAPSHOT.jar ./ops/dockerfile/applications/$deploy_env/
                        echo " file copy end"
                        pwd
                        ls -l ./ops/dockerfile/
                    '''

                    echo "TODO need make it be a function to build docker image"
                    sh '''
                        set +x
                        cd ./ops/dockerfile/applications/$deploy_env/
                        echo "TODO: check if image existing now"
                        imageExisting = $((docker inspect sre-test:v1 > /dev/null 2>&1 && echo found) || echo notfound)
                        docker images | grep "sre-test" | awk '{print $3}' | xargs docker rmi
                        docker build . -t sre-test:v1 -f sre-test.Dockerfile
                        echo "TODO: check whether this image works, if true, push to Harbo"
                    '''
                }
            }
        }

        stage('image checkin') {
            steps {
                echo "TODO: since no Harbor now, will save/load image to worker node for simulation"
                sh '''
                    set +x
                    echo "TODO: just comment for now"
                    #docker save sre-test:v1 -o sre-test-v1.tar
                    #scp sre-test-v1.tar root@vm114:/root/xjm/
                    ssh vm114 ls -l /root/xjm
                    #ssh vm114 docker load -i /root/xjm/sre-test-v1.tar
                    ssh vm114 docker image ls | grep sre-test
                '''
            }
        }

        stage("deploy to k8s") {
            steps {
                echo "TODO 1. check required provider"
                sh '''
                    set +x
                    
                '''

                echo "deploy to k8s using terraform"
                dir("${env.WORKSPACE}") {
                    sh '''
                        set +x
                        cd ./ops/terraform/$deploy_env/sre-test
                        if [ ! -L ".terraform" ]; then 
                            echo "make link to /root/xjm/tfplugin"
                            ln -s /root/xjm/tfplugin .terraform
                        else
                            echo ".terraform existing"
                        fi
                        terraform apply -auto-approve
                    '''
                }

            }
        }

        stage("Health check") {
            steps {
                sh '''
                    set +x
                    s=$(curl http://110.110.110.114:28909/xjming)
                    if [ "$s" == "welcome, xjming" ]; then
                        echo "fine"
                    else
                        echo "bad"
                    fi
                '''
            }
        }

        stage("IAST & DAST") {
            steps {
                echo "iast and dast"
            }
        }

        stage('Send notification') {
            steps {
                echo "send out report"
            }
        }
    }
}