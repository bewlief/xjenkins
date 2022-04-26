@Library("dse-jobs") _


def jobs = ["joba", "jobb", "jobc"]
def g_var1 = "hello g_var1"


pipeline {
    agent any

    environment {
        env1 = "show stage input no"
    }

    stages {
        stage("stagwe 1") {
            steps {
                script {
//                    xjm()
//                    xjm.test()
                    ACTION = "${g_var1}"
                    echo "global var1: ${g_var1}"
                    echo "-----${env.JOB_NAME}"
                    sh """
    ls -l .
    chmod 755 lib2.sh
"""
                    // 注意sh的路径，需要指明！！！
                    // returnStatus:返回sh执行后的状态值，0=正常
                    // returnStdout:sh执行中的console输出
                    // 可以在sh中使用returnStdout+echo来设置返回的数据
                    // 如果有多个echo，则最后的echo为返回值
                    def result = sh(returnStdout: true, script: "./lib2.sh")
                    echo "${result}"
                }
            }
        }

        stage("checkout") {
            steps {
                script {
                    xjm.checkoutGitx("https://gitee.com/bewlief/sre-app.git", "x1", "x1")
                    sh("ls -l")
                    def bid = xjm.getBuildId("hell")
                    log.info(">>>>> ${bid}")
                }
            }
        }

        stage('Example') {
            when {
                // branch: 仅用于多分枝job
//                branch: "kkkk"
                environment name: "env1", value: "show stage input"
            }
            steps {
                echo "in Example"
            }
//            input {
//                message "Should we continue?"
//                ok "Yes, we should."
//                submitter "alice,bob"
//                parameters {
//                    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
//                }
//            }
//            steps {
//                echo "Hello, ${PERSON}, nice to meet you."
//
//                script {
//                    env.RELEASE_SCOPE = input message: 'User input required', ok: 'Release!',
//                            parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
//                }
//                echo "${env.RELEASE_SCOPE}"
//            }
        }


    }
}