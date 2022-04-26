#!groovy

@Library('dse-jobs') _

def tools = new devops.common.utils()
tools.PrintMes("red", "this is a test msgg")

String workspace = "/opt/jenkins/workspace"

//Pipeline
pipeline {
    agent {
        node {
            label "master"   //指定运行节点的标签或者名称
            customWorkspace "${workspace}"   //指定运行工作目录（可选）
        }
    }

    options {
//        timestamps()  //日志会有时间
        skipDefaultCheckout()  //删除隐式checkout scm语句
        disableConcurrentBuilds() //禁止并行
        timeout(time: 1, unit: 'HOURS')  //流水线超时设置1h
    }
    environment {
        quick_test = false
    }
    stages {
        stage('Example') {
            input {
                message "Should we continue?"
                ok "Yes, we should."
                submitter "admin,anthony"
                parameters {
                    string(name: 'PERSON', defaultValue: 'Mr Anthony', description: 'Who should I say hello to?')
                }
            }
            steps {
                echo "Hello, ${PERSON}, nice to meet you."
            }
        }
        stage('Example Deploy') {
            when {
                expression {
                    return (quick_test == "true")

                }
            }
            steps {
                echo 'Deploying'
            }
        }

        //下载代码
        stage("GetCode") { //阶段名称
            when { environment name: 'test', value: 'abcd' }
            steps {  //步骤
                timeout(time: 5, unit: "MINUTES") {   //步骤超时时间
                    script { //填写运行代码
                        println('获取代码')
                        tools.PrintMes("获取代码", 'green')
                        println("${test}")
                        input {
                            message "Should we continue?"
                            ok "Yes, we should."
                            submitter "admin,anthony"
                            parameters {
                                string(name: 'PERSON', defaultValue: 'Mr Anthony', description: 'Who should I say hello to?')
                            }
                        }
                    }
                }
            }
        }

        stage("01") {
            failFast true
            parallel {

                //构建
                stage("Build") {
                    steps {
                        timeout(time: 20, unit: "MINUTES") {
                            script {
                                println('应用打包')
                                tools.PrintMes("应用打包", 'green')
//                                mvnHome = tool "m2"
                                println("use maven")

//                                sh "${mvnHome}/bin/mvn --version"
                            }
                        }
                    }
                }

                //代码扫描
                stage("CodeScan") {
                    steps {
                        timeout(time: 30, unit: "MINUTES") {
                            script {
                                print("代码扫描")
                                tools.PrintMes("代码扫描", 'green')
                            }
                        }
                    }
                }
            }
        }
    }

    //构建后操作
    post {
        always {
            script {
                println("always")
            }
        }

        success {
            script {
                currentBuild.description = "\n 构建成功!"
            }
        }

        failure {
            script {
                currentBuild.description = "\n 构建失败!"
            }
        }

        aborted {
            script {
                currentBuild.description = "\n 构建取消!"
            }
        }
    }
}