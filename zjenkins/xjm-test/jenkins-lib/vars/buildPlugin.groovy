/**
 * Jenkins User Handbook.pdf, P70
 * @param body
 * 这里要注意怎么传递body进来的！
 * 此脚本中的stage，在jenkins里面并没有显示出来，奇怪！
 */
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // 还是使用stage最好
    stage("stage in buildPlugin") {
//    node{
        script {
            echo "load from resource file, ${env.WORKSPACE}"
//            def dd = libraryResource "/resources/apps/dev/test.json"
//            echo "${dd}"
            echo "action then ${config.name}"
        }
    }
}