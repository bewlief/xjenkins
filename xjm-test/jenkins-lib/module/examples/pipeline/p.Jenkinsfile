package pipeline

def actionTest = null
loadList = ["beijing", "chengdu", "xxx", "shanghai", " xxx", "xxx", "11.11.11.111", "99.88.88.88"] //这里要定义要自动生成的机器label（该label是jenkins节点机的label名称）
actionTest = loadList.collectEntries {
    ["压测 ${it}": load_test(it)] //这里${it}我没搞懂（可能类似于一个遍历循环），反正按着这个来就ok,然后把it传入到方法中，然后在方法中把ip传入node
}

def load_test(nodeIp) {
    return {
        node(nodeIp) {
            dir('/data/soft/jenkins_node_release/workspace/testfiles/libs') {
                //sh "mvn install:install-file -Dfile=jmeter-plugins-dubbo-1.3.8-jar-with-dependencies.jar -DgroupId=io.github.ningyu -DartifactId=jmeter-plugins-dubbo -Dversion=1.3.8 -Dpackaging=jar"
                //sh "mvn install:install-file -Dfile=mysql-connector-java-5.1.47-bin.jar -DgroupId=mysql -DartifactId=mysql-connector-java -Dversion=5.1.47 -Dpackaging=jar"
                sh "mvn install:install-file -Dfile=com.belle.test.jmeterUtil.jar -DgroupId=com.belle.test -DartifactId=jmeterUtil -Dversion=1.0.0 -Dpackaging=jar"
                //sh "mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar"
                //sh "mvn install:install-file -Dfile=jmeter-plugins-random-csv-data-set-0.6.jar -DgroupId=com.blazemeter -DartifactId=jmeter-plugins-random-csv-data-set -Dversion=0.6 -Dpackaging=jar"
            }
            dir("${workspace}") {
                sh "pwd"
                print("开始执行测试...")
                sh "mvn clean verify -Djmx.filesDirectory.dir=${filesDirectory}  -Dargs.filesDirectory.dir=${filesDirectory}   -Djmeter.threadCount=${threadCount}    -Djmeter.duration=${duration} -Djmeter.loopCount=${loopCount}   -PLoad"
                echo '压测脚本执行完成'
            }
        }
    }
}

pipeline {
    agent {
        node {
            label 'master'
        }
    }
    options {
        timeout(time: 60, unit: 'MINUTES')
        ansiColor('xterm')
        disableConcurrentBuilds()
    }
    //此处即可开始进行代码部署操作
    stages {
        stage("执行测试") {
            steps {
                script {
                    //
                    parallel actionTest //调用入口
                }
            }
        }
    }
}