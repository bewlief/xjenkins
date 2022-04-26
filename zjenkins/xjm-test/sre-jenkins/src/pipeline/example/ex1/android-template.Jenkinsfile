// android项目

// 按需求更改变量的值

node {
    // 参数设置
    def repoUrl = 'git@*****.git'
    def repoBranch = 'develop'
    def gitDir = ''
    def workspace = 'Demo/'
    def version = ''
    def recipients = ''
    def jenkinsHost = ''    // jenkins服务器地址

    // 认证ID
    def gitCredentialsId = 'aa651463-c335-4488-8ff0-b82b87e11c59'
    def soundsId = '69c344f1-8b11-47a1-a3b6-dfa423b94d78'

    // 工具配置
    def gradleTool = './gradlew'
    // env.GRADLE_HOME = "${tool 'Gradle3.3'}"
    // env.PATH="${env.GRADLE_HOME}/bin:${env.PATH}"

    try {
        // 正常构建流程
        stage("Preparation"){
            // 拉取需要构建的代码
            checkout scm: [$class: 'GitSCM', branches: [[name: "*/${repoBranch}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${gitDir}"]], userRemoteConfigs: [[credentialsId: "${gitCredentialsId}", url: "${repoUrl}"]]]
        }
        try {
            stage('UnitTest') {
                // 在清空前一次构建的结果后进行单元测试，并获取测试报告
                dir("${workspace}"){
                    sh "${gradleTool} clean"
                    sh "${gradleTool} testReleaseUnitTest --stacktrace"
                }
            }
            stage('Build') {
                // 使用Gradlew进行sdk的构建
                dir("${workspace}"){
                    sh "${gradleTool} makeJar"
                }
            }
        }
        finally {
            stage('Results'){
                // 获取单元测试报告
                sh """
                cd ${workspace}librasdk/build/reports/tests/
                tar -zcvf test_reports.tar.gz ./test*/
                """
                archiveArtifacts allowEmptyArchive: true, artifacts: "${workspace}librasdk/build/reports/tests/test_reports.tar.gz"
                // 获取sdk构建生成的jar包
                archive "${workspace}librasdk/build/libs/*/*.jar"
            }
        }
        httpRequest authentication:"${soundsId}", url:'http://10.38.162.13:8081/sounds/playSound?src=file:///var/jenkins_home/sounds/4579.wav'
        emailext attachlog:true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT', to: "${recipients}"
    } catch(err) {
        // 构建失败
        currentBuild.result = 'FAILURE'
        httpRequest authentication:"${soundsId}", url:'http://${jenkinsHost}/sounds/playSound?src=file:///var/jenkins_home/sounds/8923.wav'
        emailext attachlog:true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT', to: "${recipients}"
    }
}