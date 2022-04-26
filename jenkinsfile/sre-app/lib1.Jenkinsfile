@Library("dse-jobs") _


def jobs = ["joba", "jobb", "jobc"]
pipeline {
    agent any

    stages {
        stage("stagwe 1") {
            steps {
                script {
                    xjm()
                    xjm.test()

                    echo "-----${env.JOB_NAME}"
                }
            }
        }


        stage("stage 2") {
            steps {
                script {
//                    def c = new devops.common.common()
//                    c.checkoutGit("https://gitee.com/bewlief/sre-app.git")

                    // 函数调用时，参数一定要和定义时相匹配！
//                    xjm.checkoutGitx("https://gitee.com/bewlief/sre-app.git", "test")
                    sh "pwd"
//                    #sh "ls -lR"
                }
            }
        }

        stage("send email") {
            steps {
                script {
                    xjm.sendEmail("1111", "subject is here", "body here", mimeType = "text/html")

                    echo "withCredential"
                    withCredentials([
                            usernamePassword(credentialsId: 'bewlief-gitee', passwordVariable: 'pass1', usernameVariable: 'user1'),
                            usernamePassword(credentialsId: 'bewlief-gitee', passwordVariable: 'ffff', usernameVariable: 'www')
                    ]) {
                        // some block
                        //TODO 如果crendential的username/password有一个为空，则会报错
                        echo "hello world"
                    }
                }
            }
        }

//        stage("trigger another job") {
//            steps {
//                // TODO 触发另一job,绝对路劲可以，相对路径如何使用？
//                build job: '/jobc',
//                        parameters: [string(name: 'name', value: "xnjm"),
//                                     string(name: 'age', value: "100")]
//            }
//        }

        stage("for loop") {
            steps {
                script {
                    for (job in jobs) {
                        stage("compile ${job}") {
                            echo "${job} compile"
                        }
                        stage("build") {
                            echo "${job} is built"
                        }
                    }
                }
            }
        }

        stage("parallel test") {
            steps {
                script {
                    def branches = [:]
                    MAX_CONCURRNT = 2
                    latch = new java.util.concurrent.LinkedBlockingDeque(MAX_CONCURRNT)
                    for (int i = 0; i < MAX_CONCURRNT; i++)
                        latch.offer("$i")

                    def job_list = ["test1", "test2", "test3", "test4", "test5", "test6"]
                    for (int i = 0; i < job_list.size(); i++) {
                        def name = job_list[i]
                        branches[name] = {
                            def thing = null
                            waitUntil {
                                thing = latch.pollFirst();
                                return thing != null;
                            }
                            try {
//                                build(job: name, propagate: false)
                                echo "run job ${name} now ......"
                            }
                            finally {
                                latch.offer(thing)
                            }
                        }
                    }
                    timestamps {
                        parallel branches
                    }
                }
            }
        }

        stage('并行执行的 Stage') {
            parallel {
                stage('Stage2.1') {
                    agent { label "master" }
                    steps {
                        timestamps {
                            echo "在 agent test2 上执行的并行任务 1."
                            sleep 5
                            echo "在 agent test2 上执行的并行任务 1 结束."
                        }
                    }
                }
                stage('Stage2.2') {
                    agent { label "master" }
                    steps {
                        timestamps {
                            echo "在 agent test3 上执行的并行任务 2."
                            sleep 10
                            echo "在 agent test3 上执行的并行任务 2 结束."
                        }
                    }
                }
            }
        }
    }
}