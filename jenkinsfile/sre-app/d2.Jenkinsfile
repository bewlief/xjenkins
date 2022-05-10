@Library("XOPS-jobs") _

def POD_LABEL = "jenkins-slave"
//def POD_LABEL = "testpod-${UUID.randomUUID().toString()}"
podTemplate(label: POD_LABEL, cloud: 'kubernetes', containers: [
        containerTemplate(name: 'build', image: 'mysql', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'run', image: 'golang:1.8.0', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'maven', image: 'maven:3.6.3-jdk-8', ttyEnabled: true, command: 'cat')
]) {

/**
 * label匹配上jenkins中设置的pod label后，下面则不能再使用container("build")，
 * pod的name和label，并不是自动创建的pod的名称和name，只是用于jenkins查找模板时使用。
 * k8s创建的pod的name是pod模板的name+随机字符串的组合
 * 而是由kubernetes来指定使用到的容器。
 * 容器模板中container 名称必须为jnlp时，才会使用设置的image类型，否额为默认的jenkins/jnlp-agent
 */
    node(POD_LABEL) {
        stage('build a go project') {
//            container('build') {
            stage('Build a go project') {
                sh 'echo hello'
            }
//            }
        }

        stage('Run a Golang project') {

//            container('run') {
            stage('Run a Go project') {
                echo "run a golang project"
            }
//            }
        }

        stage("maven") {
            xjm.checkoutGitx("https://gitee.com/bewlief/sre-app.git", "dev", ".")
            container("maven") {
//
//                checkout([
//                        $class                           : 'GitSCM',
//                        branches                         : [[name: "*/dev"]],
//                        doGenerateSubmoduleConfigurations: false,
//                        extensions                       : [],
//                        submoduleCfg                     : [],
//                        userRemoteConfigs                : [
//                                [
//                                        credentialsId: 'bewlief-gitee',
//                                        url          : "https://gitee.com/bewlief/sre-app.git"
//                                ]
//                        ]
//                ])
                sh """
                    mvn --version
                    pwd
                    ls -lR
                """
            }
        }

    }
}

