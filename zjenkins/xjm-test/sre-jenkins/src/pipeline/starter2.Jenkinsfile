#!groovy
package pipeline

import com.lesfurets.jenkins.unit.global.lib.Library

@Library('jenkinslib@dev') _

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
job_name = "${env.JOB_NAME}".replace("%2F", "/").replace("-", "/").replace("_", "/")
job_name = job_name[0].toLowerCase()
workspace = "workspace/${job_name}/${env.BRANCH_NAME}"

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
                    println "Hello Pipeline!"
                    println env.JOB_NAME
                    println env.BUILD_NUMBER
                    println env.WORKSPACE
                    println env.JENKINS_HOME
                    tools.message(env.JOB_NAME, "red")
                    test.java_func("hello xjm")
                }

                script {
                    println "load from test.json"
                    jsonFile = "${env.WORKSPACE}/resources/apps/dev/test.json"
                    prop = readJSON file: jsonFile
                    name = prop.NAME ? prop.NAME.trim() : "NONAME"
                    println "Name: " + name
                }

                script {
                    // 动态读入代码文件并执行其中的函数
                    println "load functions from test.groovy"
                    script_test = load env.WORKSPACE + "/src/com/xhoe/ops/test.groovy";
                    println script_test.java_func("helo xjm")
                    sreTestJob()
                }

                script {
//                    println "call pipeline app1-dev"
                    // build job是调用jenkins job，而不是直接调用pipeline
//                    build job: "app1-dev.Jenkinsfile"

                    // 直接调用vars/job1.groovy
                    job1()
                }
            }
        }

    }
}