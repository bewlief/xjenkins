#!groovy

package pipeline

pipeline {
    //在任何可用的代理上执行Pipeline
    agent any

    //环境变量，初始确定后一般不需更改
    tools {
        maven 'maven'
        jdk 'jdk8'
    }
    //常量参数，初始确定后一般不需更改
    environment {
        //git服务全系统只读账号cred_id【参数值对外隐藏】
        CRED_ID = '*****-****-****-****-*********'
        //测试人员邮箱地址【参数值对外隐藏】
        QA_EMAIL = '*****@*****.com'
        //接口测试（网络层）的job名，一般由测试人员编写
        ITEST_JOBNAME = 'Guahao_InterfaceTest_ExpertPatient'
    }
    options {
        //保持构建的最大个数
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    //定期检查开发代码更新，工作日每晚4点做daily build
    triggers {
        pollSCM('H 4 * * 1-5')
    }
    //pipeline运行结果通知给触发者
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

    //pipeline的各个阶段场景
    stages {
        stage('代码获取') {
            steps {
                //根据param.server分割获取参数,包括IP,jettyPort,username,password
                echo "git checkout codes"
            }
        }

        stage("static check") {
            parallel {
                stage("blank") {
                    steps {
                        echo "just a blank stage"
                    }
                }
//                stage('单元测试') {
//                    steps {
//                        echo "starting unitTest......"
//                    }
//                }
//                stage('静态检查') {
//                    steps {
//                        echo "starting codeAnalyze with SonarQube......"
//                    }
//                }
            }
        }

        stage('部署测试环境') {
            steps {
                echo "deploy to test"
            }
        }

        stage("auto testing") {

            parallel {

                stage("blank") {
                    steps {
                        echo "just a blank stage"
                    }
                }
                stage('接口自动化测试') {
                    steps {
                        echo "starting interfaceTest......"
                    }
                }

                stage('UI自动化测试') {
                    steps {
                        echo "starting UITest......"
                        //这个项目不需要UI层测试，UI自动化与接口测试的pipeline脚本类似
                    }
                }
            }
        }


        stage('性能自动化测试 ') {
            steps {
                echo "starting performanceTest......"
                //视项目需要增加性能的冒烟测试，具体实现后续专文阐述
            }
        }

        stage('通知人工验收') {
            steps {
                echo "send out report"
            }
        }

        stage('发布到生产环境') {
            steps {
                echo "starting deploy......"
                //    TODO发布环节后续专题阐述
            }
        }
    }
}