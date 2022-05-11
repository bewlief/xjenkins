#!groovy
package pipeline
/**
 * 注意Libarary的使用！多个job里都设置时，会git clone多次这个library！
 * 因此，应该使用一个独立的git repo管理pipeline和library，而不是把pipeline放到library
 * 下的vars/下
 */
@Library('jenkinslib@master') _

/**
 * 被starter调用，此时无法使用Jenkins library！
 */
def tools = new com.xhoe.ops.tools()
def email = new com.xhoe.ops.toemail()
def test = new com.xhoe.ops.test()

println ">>>>>> env parameters"
println env.BUILD_NUMBER

// 此时只能取得JOB_NAME，无法获取WORKSPACE
def jobName = env.JOB_NAME
def rootDir = env.WORKSPACE

// start可以接收参数
def start(msg) {
    echo "job1 start $msg"

    println "Hello Pipeline!"
    println env.JOB_NAME
    println env.BUILD_NUMBER
    println env.WORKSPACE
    println env.JENKINS_HOME
    tools.message("what's taht", "red")
    test.java_func("helo java faunc")
}

return this;