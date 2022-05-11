#!groovy
package pipeline

import com.lesfurets.jenkins.unit.global.lib.Library

@Library('jenkinslib@dev') _

def tools = new com.xhoe.ops.tools()
def email = new com.xhoe.ops.toemail()
def test = new com.xhoe.ops.test()

println ">>>>>> env parameters"
println env.BUILD_NUMBER
println env.JOB_NAME
println env.WORKSPACE

pipeline {
    agent any
//    agent {
//        node {
//            label "vm113"
//            customWorkspace "${env.JOB_NAME}/${env.BUILD_NUMBER}"
//        }
//    }

    environment {
        CRED_ID = '*****-****-****-****-*********'
        QA_EMAIL = '*****@*****.com'
        ITEST_JOBNAME = 'x1'

    }

    triggers {
        // scm
//        pollSCM('H H(9-16)/2 * * 1-5)')

        // upstream
//        upstream(upstreamProjects: 'job1,job2', threshold: hudson.model.Result.SUCCESS)

        // cron
        cron('H(0-29)/10 * * * *')
    }

//    parameters {
//        password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'A secret password')
//    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '1'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        stage("test1") {
            steps {
                script {
                    echo CRED_ID
                }
            }
        }
        stage("Hello Pipeline") {
//            input {
//                message "Should we continue?"
//                ok "Yes, we should."
//                submitter "admin,anthony"
//                parameters {
//                    string(name: 'PERSON', defaultValue: 'Mr Anthony', description: 'Who should I say hello to?')
//                }
//            }

            steps {
                script {
                    // deleteDir: 清空workspace
//                    deleteDir()
                    println "Hello Pipeline!"
                    println env.JOB_NAME
                    println env.BUILD_NUMBER

                    println "find *.groovy files"
                    def files = findFiles(glob: "**/*.groovy")
                    echo files[0].name

                    log_files = test.find_files("**/*.groovy")

                    test.java_func("hello you")
                }

                script {
                    println "load from test.json"
                    jsonFile = "${env.WORKSPACE}/resources/test.json"
                    prop = readJSON file: jsonFile
                    name = prop.NAME ? prop.NAME.trim() : "NONAME"
                    println "Name: " + name
                }
            }
        }

        stage("just test") {
            steps {
                script {
                    println("get code from git now ")
                    tools.message("get code", "green")
                }
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