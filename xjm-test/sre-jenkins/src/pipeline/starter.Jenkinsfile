#!groovy
package pipeline

/**
 * shared library的引入，2种方式
 * 1. 在jenkins中设置jenkslib，使用scm
 */
@Library('jenkinslib@master') _
//library identifier: 'sre-jenkins@dev', retriever: modernSCM(
//        [$class       : 'GitSCMSource',
//         remote       : 'https://gitee.com/bewlief/sre-jenkins.git',
//         credentialsId: '71935665-gitee'])
//@Library('gitee.com/bewlief/sre-jenkins@dev') _


def tools = new com.xhoe.ops.tools()
def email = new com.xhoe.ops.toemail()
def test = new com.xhoe.ops.test()

println ">>>>>> env parameters"
println env.BUILD_NUMBER

// 此时只能取得JOB_NAME，无法获取WORKSPACE
def jobName = env.JOB_NAME
def rootDir = env.WORKSPACE

/**
 * 这是所有job的starter，路径固定为pipeline/starter.Jenkinsfile
 * 根据jobName获取要调用的function/pipeline,在piprline/dev/...
 * jobName格式要固定，如：user-service_dev，应用名称-env，user-service+uat等
 * 然后读取对应的配置文件，resources/apps/dev/user-server.json|yaml|ini，
 * 只使用一个格式的文件，如json，yaml
 */

// 该应用-env的配置文件
def configFile

// 该应用-env的脚本文件
def scriptFile

def job_name = ""
def jenkinsFile = ""

echo env.JOB_NAME
//job_name = "${env.JOB_NAME}".replace("%2F", "/").replace("-", "/").replace("_", "/")
job_name = env.JOB_NAME
echo "--->>> $job_name"
//job_name = job_name[0].toLowerCase()
workspace = "workspace/${job_name}/${env.BRANCH_NAME}"

// 使用node节去调用job1，无stage view
//node{
//
//    def check_groovy_file = "pipeline/jobs/${job_name}.Jenkinsfile"
//    jenkinsFile = load "${check_groovy_file}"
//    jenkinsFile.start()
//}

pipeline {
    agent any

    environment {
        CRED_ID = '*****-****-****-****-*********'
        QA_EMAIL = '*****@*****.com'
        ITEST_JOBNAME = 'x1'

    }


    options {
        buildDiscarder(logRotator(numToKeepStr: '1'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')

        // 禁止同时运行多个流水线
        disableConcurrentBuilds()
    }
    // 下面的这2个script{}都会报错！
//    script {
//        println "before stages "
//    }
    stages {
        stage("Hello Pipeline") {
//            script {
//                println "before steps "
//            }
            steps {
                script {
                    email.msg("hello email")
                    tools.message("message here", "green")
                    def check_groovy_file = "pipeline/jobs/${job_name}.Jenkinsfile"
                    jenkinsFile = load "${check_groovy_file}"
                    // 调用job1.Jenkinsfile，可以传递参数过去
                    jenkinsFile.start("hellxxxxxxxxxxxxxxxxxx")
                }
            }
        }

        // 配合when，可实现动态的stage
        stage("call others") {
            steps {
                script {
                    def check_groovy_file = "pipeline/jobs/job2.Jenkinsfile"
                    jenkinsFile = load "${check_groovy_file}"
                    // 调用job1.Jenkinsfile，可以传递参数过去
                    jenkinsFile.start("hellxxxxxxxxxxxxxxxxxx")
                }
            }
        }

    }
}