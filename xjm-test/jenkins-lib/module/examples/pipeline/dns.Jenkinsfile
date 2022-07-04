#!groovy

package pipeline

/**
 * 部署DNS配置到192.168.12.25上
 *
 **/

pipeline {
    //在任何可用的代理上执行Pipeline
    agent any

    //常量参数，初始确定后一般不需更改
    environment {
        //git服务全系统只读账号cred_id【参数值对外隐藏】
        CRED_ID = '*****-****-****-****-*********'
        //测试人员邮箱地址【参数值对外隐藏】
        QA_EMAIL = '*****@*****.com'
        //接口测试（网络层）的job名，一般由测试人员编写
        ITEST_JOBNAME = 'Guahao_InterfaceTest_ExpertPatient'
    }

    //pipeline运行结果通知给触发者
    post {
        success {
            script {
                echo "success"
            }
        }
        failure {
            script {
                echo "failure"
            }

        }
        unstable {
            script {
                echo "unstable"
            }
        }
    }

    //pipeline的各个阶段场景
    stages {
        stage('代码获取') {
            steps {
                echo "starting fetchCode from ${params.repoUrl}......"
                // Get some code from a GitHub repository
//                git credentialsId: CRED_ID, url: params.repoUrl, branch: params.repoBranch
            }
        }
        stage('配置文件上传') {
            steps {
                echo "starting unitTest......"
            }
        }
        stage('重启named') {
            steps {
                echo "starting codeAnalyze with SonarQube......"

                script {
                    echo "restart named"
                }
            }
        }

        stage('测试') {
            steps {
                echo "starting testing......"
            }
        }
    }
}