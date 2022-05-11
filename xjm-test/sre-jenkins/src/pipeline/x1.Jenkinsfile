#!groovy
package pipeline

@Library('jenkinslib@master') _

def email = new com.xhoe.ops.toemail()
def common = new com.xhoe.ops.CommonFunction()

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
                    common.message("get code", "green")
                }
            }
        }

        stage('checkout source codes') {
            steps {
                // echo "git checkout codes from $app_repo: $app_branch, deploy to $deploy_env "
                checkout([
                        $class                           : 'GitSCM',
                        branches                         : [[name: "*/dev"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [[$class: 'RelativeTargetDirectory', relativeTargetDir: './sre-app']],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [
                                [credentialsId: '', url: "https://gitee.com/bewlief/sre-app.git"]
                        ]
                ])
                echo "TODO: retrieve application name/version from POM"
                sh '''
                    set +x
                    cd ./sre-app/
                    pwd
                    $MAVEN_HOME/bin/mvn clean package -Dmaven.test.skip=true
                '''
                script {
                    sh("cd ./sre-app")
                    def m = readMavenPom file: './sre-app/pom.xml'
                    def v = m.groupId
                    def o = m.artifactId
                    def s1 = m.properties['parent.artifactId']
                    echo "============ groupId=$v, artifactId=$o, => $s1"
                }
            }
        }
        stage("publish report") {
            steps {
                script {
                    sh("mkdir -p ./app/build/reports")
                    def s = common.isAdmin("xjming")
                    echo "$s"
                    if (common.isAdmin("xjming")) {
                        common.message("is admin", "green")
                    } else {
                        common.message("not admin", "red")
                    }
                }
            }
        }
    }

}