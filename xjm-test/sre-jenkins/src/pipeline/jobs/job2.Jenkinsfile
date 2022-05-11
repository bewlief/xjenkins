#!groovy
package pipeline

@Library('jenkinslib@dev') _

//def tools = new com.xhoe.ops.tools()
//def email = new com.xhoe.ops.toemail()
//def test = new com.xhoe.ops.test()

println ">>>>>> env parameters"
println env.BUILD_NUMBER

// 此时只能取得JOB_NAME，无法获取WORKSPACE
def jobName = env.JOB_NAME
def rootDir = env.WORKSPACE

// start可以接收参数
// 一个run中只能有一个pipeline{}，因此这里使用node{}来返回stage
// 和starter2中调用vars/sreTestJob.groovy是类似的
def start(msg) {
    node {
        stage('job2-1111') {
            deleteDir()
            sh """
        echo "git clone finished"
      """
        }

        stage("job2-2222") {
            echo "static check now"
        }
    }
}

return this;