package pipeline

@Library('devops-library') _
//@Library('github.com/fabric8io/fabric8-pipeline-library@master') _1

pipeline {
    agent any

    parameters {
        string(name: 'projectName', defaultValue: 'eureka', description: ' git repo of onlutest1')
        string(name: 'branch', defaultValue: '9be83ad8-c558-4a7d-b830-b0784243517a')
        string(name: 'env', defaultValue: 'dev,fat,uat,pro,xjm')

        string(name: "jsonPath", defaultValue: "eureka-full", description: "configurtion of json file")

//        booleanParam(name: 'requireService', defaultValue: false, description: 'if k8s service required')
//        text(name: 'welcome', defaultValue: 'One\nTwo\nThree\n', description: "just a msg")
//        choice(name: 'Env_type', choices: ['dev', 'fat', 'uat', 'pro', 'xjm'], description: 'env for testing')
        choice(name: 'Env_type', choices: 'dev\nfat\nuat\npro\nxjm', description: 'env for testing')
//        file(name: 'configFile', description: 'here is a file')
//        password(name: 'PASSWORD', defaultValue: "SECRET", description: 'a password')
    }
    environment {
        G_DISABLE_AUTH = 'true'
        G_DB_ENGINE = 'mysql'

        G_GOGS_URL = "http://192.168.16.16:3000/codeAdmin/houtaixitong.git"
        G_CREDENTIAL_ID = "9be83ad8-c558-4a7d-b830-b0784243517a"

        G_DOCKER_REGISTRY_URL = "192.168.22.99"
        G_DOCKER_REGISTRY_CREDENTIAL_ID = "598c2b7f-6ebd-4134-bb21-8d34f841ca8c"

        G_ENV = "dev111"
    }

    tools {
        maven 'maven'
        jdk 'jdk8'
    }

    triggers {
        pollSCM('H/3 * * * 1-5')
    }


    stages {

        stage("init") {
            steps {
                script {
                    sh("chmod 755 ${env.WORKSPACE}/src/pipeline/shells/*")
                }
            }

        }

        stage("parallel stags test") {
            failFast true
            parallel {
                stage("parallel1") {
                    steps {
                        echo "parallel stage 1"
                        echo "currentBuild.getNumber: ${currentBuild.getNumber()}"
                        sh(env.WORKSPACE + "/src/pipeline/shells/1.sh")
                    }
                }
                stage("parallel2") {
                    steps {
                        echo "parallel stage 2"
                    }
                }
            }
        }


        stage("pipeline-libray test") {

            steps {
                script {
                    def TIMESTAMP = getTimeStamp();
                    echo "from shared library: ${TIMESTAMP}"


                    /**
                     * 可以把各应用的配置文件放到指定目录下，通过指定文件名称去引用
                     *
                     */
                    echo "read json file from jsonPath"
                    json_file = params.jsonPath ? params.jsonPath.trim() : ""
                    json_file = env.WORKSPACE + "/src/apps/${json_file}.json"
                    echo ">>>>>> read from ${json_file}"
                    // 检查文件是否存在
                    if (fileExists(json_file) == true) {
                        def props = readJSON file: json_file
                        echo "${props.branch},${props.repoUrl},${props.stages}"
                    } else {
                        error("no ${json_file} existing, ABORT")
                    }

//                    m = load env.WORKSPACE + "/src/pipeline.module/Utils.groovy"
//                    def ki = m.pipelineConfiguration()
//                    echo ">>>>> Utils.pipelineConfiguration: ${ki}"

                    d = load env.WORKSPACE + "/src/bcgov/ChangedFiles.groovy"
                    d.changed_files()

                    model_test = load env.WORKSPACE + "/src/pipeline/module/utils1.groovy";
                    json_file = env.WORKSPACE + "/src/configuration/repos.json"
                    model_test.read_json_file(json_file)
                    echo "=========================="

                    tojson_file = env.WORKSPACE + "/testdata/new_json.json"
                    model_test.write_json_to_file(json_file, tojson_file)
                    println "================================"
                    json_string = '{"NAME":"Anthony","AGE":18,"CITY":"Beijing","GENDER":"male"}'
                    tojson_file = env.WORKSPACE + "/testdata/new_json1.json"
                    // 下面这个函数由于使用了JsonOutput，需要admin的批准才行
                    // 会抛出一个错误
//                    model_test.write_json_to_file(json_string, tojson_file)

                    echo "read properties file"
                    properites_file = env.WORKSPACE + "/src/configuration/repos.properties"
                    model_test.read_properties(properites_file)

                    echo "read yaml"
                    yaml_file = env.WORKSPACE + "/src/configuration/test.yml"
                    model_test.read_yaml_file(yaml_file)

                    echo "write and read file"
//                    write_file_path = "${env.WORKSPACE}/testdata/write.txt"
//                    contents = "hello you, this is a test"
//                    writeFile file: write_file_path, text: contents, encoding: "UTF-8"
//                    fileContent = readFile file: write_file_path, encoding = "UTF-8"
//                    println fileContent

//                    try {
//                        out = sh(script: "[ -f /tmp/test1.js.w ] && echo 'true' || echo 'false'", returnStdout: true)
//                        println out
//                        if (out == "true") {
//                            println "file /tmp/test1.js.w existing"
//                        } else {
//                            sh("exit 1")
//                        }
//                    } catch (Exception e) {
//                        println e
//                        error("unable to find configuration/repos.json")
//                    }
                }

            }

        }

        stage('just a test') {
            steps {
                script {
                    // params/env的变量被引用时，如果在""中，需要用${}，否则直接用即可，如 env.PATH
                    echo "parametes: ${params.projectName}, env: ${params.env} + ${env.G_GOGS_URL}, jobname=${JOB_BASE_NAME}"
                    echo "===> ${currentBuild.displayName}"
                    echo "env.path: ${env.PATH}"
                    echo env.PATH
                    echo "WORKSAPCE=$WORKSPACE"
                    echo "currentBuild.result: ${currentBuild.result}"
                    echo "running ${env.BUILD_ID} on ${env.JENKINS_URL}"

                    sh "java -version"
                    sh "mvn -version"

                    // 设置一个变量，用于根据env去走when
                    ENV_INPUT = "${params.env}"


                    // 分解","分割的字符串
                    for (e in params.env.tokenize(',')) {
                        echo "input env: $e"

                    }
                }
            }
        }

        stage('switch') {
            steps {
                script {
                    switch (params.env) {
                        case "dev":
                            echo "dev here"
                            x_env = "dev"
                            x_branch = "dev0232"
                            break
                        case "uat":
                            echo "uat here"
                            x_env = "uat"
                            x_branch = "duat0232"
                            break
                        case "fat":
                            echo "fat here"
                            x_env = "fat"
                            x_branch = "fat232"
                            break
                        default:
                            echo "default env now"
                            x_env = "default"
                            x_branch = "default env_branch"
                            break
                    }

                    echo ">>>> $x_env, $x_branch"
                }
            }
        }

        stage('ifstage') {
            steps {
                script {
                    if (params.env == 'dev' || params.env == 'uat') {
                        echo "params.env is in (dev, uat)"
                        echo "set other parameters here"
                    } else {
                        echo "params.env not in (dev, uat)"

                    }
                }
            }
        }

        stage('when1') {
            when {
                equals expected: 'dev',
                        actual: ENV_INPUT
            }
            steps {
                sh 'echo when1 stage ...'
            }
        }

        stage("when") {
            when {
                environment name: 'G_ENV',
                        value: 'dev'
            }
            steps {
                echo "when G_ENV=dev, come here"
            }
        }

        stage('checkout') {
            steps {
                script {
                    echo "project name = ${params.projectName}"

                    git branch: 'xjm0610', credentialsId: '9be83ad8-c558-4a7d-b830-b0784243517a', url: 'http://192.168.16.16:3000/xjming/onlytest1'

                    def pom = readMavenPom file: "pom.xml"
                    echo ">>>> ${pom.groupId}, ${pom.packaging}, ${pom.version}"

                    checkout([
                            $class                           : 'GitSCM',
                            branches                         : [[name: '*/master']],
                            doGenerateSubmoduleConfigurations: false,
                            extensions                       : [[$class: 'RelativeTargetDirectory', relativeTargetDir: '/root/xjm/p1/jscript']],
                            submoduleCfg                     : [],
                            userRemoteConfigs                : [
                                    [credentialsId: '9be83ad8-c558-4a7d-b830-b0784243517a',
                                     url          : 'http://192.168.16.16:3000/tmadmin/eureka-service.git']
                            ]
                    ])


                    checkout([$class: 'GitSCM', branches: [[name: '*/xjm2']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: '/root/xjm/p1/ops']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '9be83ad8-c558-4a7d-b830-b0784243517a', url: 'http://192.168.16.16:3000/tmadmin/operation.git']]])
                    sh("cd /root/xjm/p1/ops")
                    shortCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
                    echo ">>> shortCommit=${shortCommit}"

                    commitChangeset = sh(returnStdout: true, script: 'git diff-tree --no-commit-id --name-status -r HEAD').trim()
                    echo ">>> commitChangeset in this commit: ${commitChangeset}"


                    // push to git
//                    withCredentials([usernamePassword(credentialsId: 'git-pass-credentials-ID', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
//                        sh("git tag -a some_tag -m 'Jenkins'")
//                        sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@<REPO> --tags')
//                    }
//
// For SSH private key authentication, try the sshagent step from the SSH Agent plugin.
//                    sshagent (credentials: ['git-ssh-credentials-ID']) {
//                        sh("git tag -a some_tag -m 'Jenkins'")
//                        sh('git push <REPO> --tags')
//                    }

                    echo "util of findFiles"
                    sh "`pwd`"
                    echo "CUrrent dir: `pwd`"
                    // findFiles需要 pipeline utility steps这个插件！
                    def files = findFiles(glob: "**/*")
                    echo files[0].name

                    echo 'TODO check if codes update after last run'

                    echo "$JOB_NAME, $JOB_BASE_NAME, $WORKSPACE"
//                echo "current dir: `pwd`"
//                sh "./1.sh"

                    ansiColor('xterm') {
                        echo "in ansiColor - xterm"

                    }
                    script {
                        // executing groovy script here
                        def var = 'just a var here'
                    }

                    dir('/root/xjm') {
                        echo "current is: `pwd`"
                    }
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
                echo "tm2 is here "
                // sh 'printenv'
                sh '''
					mvn clean package
				'''
            }
        }

        stage('Test') {
            steps {
                retry(3) {
                    echo 'retry something here'
                }


                //sonar:sonar.QualityGate should pass
//                withSonarQubeEnv('Sonar-6.4') {
//                    //固定使用项目根目录${basedir}下的pom.xml进行代码检查
//                    //sh "mvn -f pom.xml clean compile sonar:sonar"
//                    sh "mvn sonar:sonar " +
//                            "-Dsonar.sourceEncoding=UTF-8 "//+
//                    //"-Dsonar.language=java,groovy,xml"+
//                    //"-Dsonar.projectVersion=${v} "+
//                    //"-Dsonar.projectKey=${JOB_NAME} "+
//                    //"-Dsonar.projectName=${JOB_NAME}"
//                }
//                script {
//                    //  未通过代码检查，中断
//                    timeout(10) {
//                        //利用sonar webhook功能通知pipeline代码检测结果，未通过质量阈，pipeline将会fail
//                        def qg = waitForQualityGate()
//                        if (qg.status != 'OK') {
//                            error "未通过Sonarqube的代码质量阈检查，请及时修改！failure: ${qg.status}"
//                        }
//                    }
//                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                sh '''
					pwd
					echo `pwd`
					# ls -lR .
					#mvn deploy
				'''

//                script {
//                    def env_split = parameters.env.split(",")
//                    e1 = env_split[0]
//                    echo "first env: e1"
//                }

                script {
                    echo "till now, currentBuild result= currentBuild.result"
                    if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
                        echo "will publish or deploy then"

                    }
                }
            }
        }

        stage('docker deplloy') {
            steps {
                script {

                    ansiColor('xterm') {
//                        docker.withRegistry(REGISTRY_URL, REGISTRY_CREDENTIALS_ID) {
//                            def imgName = "${REGISTRY_DOMAIN}/${DOCKER_NAMESPACE}/${project_name}:${tagName}";
//
//                            for (item in ipList.tokenize(',')) {
//                                def sshServer = getServer(item)
//
//                                // 更新或下载镜像
//                                sshCommand remote: sshServer, command: "docker pull ${imgName}"
//
//                                try {
//                                    // 停止容器
//                                    sshCommand remote: sshServer, command: "docker stop ${project_name}"
//                                    // 删除容器
//                                    sshCommand remote: sshServer, command: "docker rm -f ${project_name}"
//                                } catch (ex) {
//                                }
//
//                                // 启动容器
//                                sshCommand remote: sshServer, command: "docker run -d --name ${project_name} -e TZ=Asia/Shanghai ${imgName}"
//
//                                // 清理none镜像
//                                def clearNoneSSH = "n=`docker images | grep  '<none>' | wc -l`; if [ \$n -gt 0 ]; then docker rmi `docker images | grep  '<none>' | awk '{print \$3}'`; fi"
//                                sshCommand remote: sshServer, command: "${clearNoneSSH}"
//                            }
//                        }
                    }
                }
            }
        }

        stage('Archive') {
            steps {
                // 归档文件
                /*archiveArtifacts artifacts: 'target/*.jar,target/alternateLocation/*.*,'+'target/classes/*.*,target/classes/i18n/*.*,target/classes/rawSQL/*.*,'+'target/classes/rawSQL/mapper/*.*,target/classes/rawSQL/mysql/*.*,'+'target/classes/rawSQL/sqlserver/*.*',fingerprint: true*/
//                archiveArtifacts params.deployLocation
                echo "archive now"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

    }

    post {
        always {
            echo 'This will always run'
            echo 'clear workspace ......'
//            deleteDir()
        }
        success {
            echo 'This will run only if successful'
        }
//        failure {
//            echo 'This will run only if failed'
//            mail to: 'jm_xin@taimei-air.com',
//                    subject: 'Failed pipeline : ${currentBuild.fullDisplayName}',
//                    body: 'Something wrong with ${env.BUILD_URL}'
//        }
//        unstable {
//            echo 'This will run only if the run was marked as unstable'
//        }
//        changed {
//            echo 'This will run only if the state of the Pipeline has changed'
//            echo 'For example, if the Pipeline was previously failing but is now successful'
//        }
    }
}