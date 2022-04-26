package pipeline

def checkServices(args) {

    /**
     * 动态生成的stage中，无法获取其parent pipeline的env.WORKSPACE，是env.WORKSPACE后面加上@2之类的
     */
//    echo "unique-starter.Jenkinsfile, func checkSErvices, current workspace= ${env.WORKSPACE}"
//    echo "G_PIPELIE_ROOT= ${env.G_PIPELINE_ROOT}"

    //并行执行，需要return，串行执行不需要return
    return {
        /**
         * 在scripts里面指定node，相当于另外指定节点机
         * 在方法体中可以写非声明式模块代码，扩展性非常强
         */
        /**
         * 并行时不会报错，串行时，报错： java.lang.IllegalArgumentException: Expected a closure or failFast but found 执行Job unit-test=null
         * 暂时无解！！！！！！！
         */
        args = args.trim()
//    node {
        stage("${args}") {
            script {
                println("当前功能：${args}")
                stage_file = "$G_PIPELINE_ROOT/src/pipeline/substages/${args}.groovy"
                echo "load stage codes from ${stage_file}"
                if (fileExists(stage_file) == true) {
                    m = load stage_file
                    m.runzzzz()
                } else {
                    echo "FAILED to load ${stage_file}, ABORT"
                }
            }
        }
//    }
// return end
    }
}

pipeline {
    agent any
    options {
        disableConcurrentBuilds() //不允许并行执行
        timeout(time: 5, unit: 'SECONDS')//设置全局超时时间。
    }


    stages {
        stage("数据准备") {
            steps {
                script {
                    /**
                     * 因动态生成的stage，及调用的api-test.groovy中无法使用当前parent pipeline的env.WORKSPACE，
                     * 在此设置一个环境变量，在子stage/脚本中可以读取该环境变量
                     */
                    env['G_PIPELINE_ROOT'] = env.WORKSPACE
                    echo "now, G_PIPELINE_ROOT=${G_PIPELINE_ROOT}"
                    echo ">>> job_name=$JOB_NAME, job_base_name=$JOB_BASE_NAME, $WORKSPACE"
                    appConfig = env.WORKSPACE + "/src/apps/${JOB_BASE_NAME}.json"
                    // 检查文件是否存在
                    def props = null
                    if (fileExists(appConfig) == true) {
                        props = readJSON file: appConfig
                        echo "${props.branch},${props.repoUrl},${props.stages}"
                    } else {
                        error("FATAL: ${appConfig} not found, ABORT")
                        exit 1
                    }

                    //声明变量，用于接收遍历list的返回至
                    def executeRobot = null

                    //定义要遍历的List
                    def labelList = props.stages.split(",")

                    //collectEntries 对list的每一个元素进行一定格式的迭代转换,返回map
                    executeRobot = labelList.collectEntries {
                        //遍历调用checkServices()
                        echo ">>>> ${it}"
                        ["执行Job ${it}": checkServices(it)]
                    }
                    executeRobot.failFast = true

                    /**
                     * parallel需要接受一个map类型的参数
                     */
                    //并行执行测试，关键字:parallel；executeRobot变量名
                    parallel executeRobot
                }
            }
        }
    }
}