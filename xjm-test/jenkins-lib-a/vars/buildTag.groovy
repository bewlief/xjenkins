def call() {
    //#JENKINS中已经存在BUILD_TAG变量，如果你不想覆盖
    env.JENKINS_TAG = "${env.BUILD_TAG}"
    env.GIT_COMMIT7 = env.GIT_COMMIT.substring(0, 8)
    env.BUILD_TAG2 = "${env.GIT_BRANCH}/${env.GIT_COMMIT7}"

//    def matcher = env.GIT_URL =~ /[/ : ] ( \ \ w + ) / ( \ \ w + ) \ \.git$ /
//    if (matcher) {
//        env.GIT_GROUP1 = matcher.group(1)
//        env.GIT_GROUP2 = matcher.group(2)
//    }
    echo env.JENKINS_TAG
    echo env.GIT_COMMIT7
    echo env.BUILD_TAG2
}
