package pipeline.examples

#!/usr/bin/env groovy

pipeline {
    //确认使用主机
    agent
            {
                kubernetes {
                    label "${BUILD_TAG}-pod"
                    defaultContainer 'jnlp'
                    yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: some-label-value
spec:
  containers:
  - name: jnlp-slave
    image: 172.17.1.XXX/library/jenkins-docker:2
    imagePullPolicy: Always
    env:
    - name: MY_NODE_NAME
      valueFrom:
        fieldRef:
          fieldPath: spec.nodeName
    - name: MY_POD_NAME
      valueFrom:
        fieldRef:
          fieldPath: metadata.name
    - name: MY_POD_NAMESPACE
      valueFrom:
        fieldRef:
          fieldPath: metadata.namespace
    - name: MY_POD_IP
      valueFrom:
        fieldRef:
          fieldPath: status.podIP
    - name: MY_POD_SERVICE_ACCOUNT
      valueFrom:
        fieldRef:
          fieldPath: spec.serviceAccountName
    volumeMounts:
    - name: sock
      mountPath: /var/run/docker.sock
    - name: mvn
      mountPath: /home/jenkins/.m2/repository      
    command:
    - cat
    tty: true
  volumes:
  - name: sock
    hostPath:
      path: /var/run/docker.sock
  - name: mvn
    glusterfs:
      endpoints: glusterfs-cluster
      path: /gfs/jenkinsmvn/.m2/repository
      readOnly: false
"""
                }
            }
    //常量参数，初始确定后一般不需更改
    environment {
        //services的pom.xml的相对路径
        pomPath = 'pom.xml'
        //gitlab账号
        GIT_USERNAME = 'jenkins'
        //密码
        GIT_PASSWORD = '*********'
        //k8s节点地址
        TESTIP = '172.17.1.xxx'
        //归档文件，jmeter测试报告
        responseData = 'jmeter/ResponseData.xml,' + 'jmeter/ResultReport/*.*,' + 'jmeter/ResultReport/sbadmin2-1.0.7/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/less/*.*,' + 'jmeter/ResultReport/sbadmin2-1.0.7/dist/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/dist/css/*.*,' + 'jmeter/ResultReport/sbadmin2-1.0.7/dist/js/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/*.*,' + 'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/bootstrap/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/bootstrap/dist/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/bootstrap/dist/css/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/bootstrap/dist/fonts/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/bootstrap/dist/js/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/metisMenu/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/metisMenu/dist/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/flot.tooltip/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/flot.tooltip/js/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/flot-axislabels/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/flot/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/font-awesome/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/font-awesome/css/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/font-awesome/less/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/font-awesome/fonts/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/font-awesome/scss/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/jquery/*.*,' +
                'jmeter/ResultReport/sbadmin2-1.0.7/bower_components/jquery/dist/*.*,' + 'jmeter/ResultReport/content/*.*,' +
                'jmeter/ResultReport/content/css/*.*,' + 'jmeter/ResultReport/content/pages/*.*,' +
                'jmeter/ResultReport/content/js/*.*'
        //jmeter测试脚本名称
        JMETERNAME = 'cms'
    }
    options {
        //保持构建的最大个数
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // 设置Pipeline运行的超时时间
        timeout(time: 1, unit: 'HOURS')
    }
    //pipeline运行结果通知给触发者
    post {
        //始终执行
        /*always {
            echo "分支${BRANCH_NAME}删除服务......"
            container('jnlp-slave') {
                sh """
		chmod 755 docker_shell/*.sh
		docker_shell/always.sh ${JOB_NAME} ${BUILD_NUMBER}
		"""
            }
        }*/
        //失败触发邮件
        failure {
            script {
                emailext body: '${JELLY_SCRIPT,template="static-analysis"}',
                        recipientProviders: [[$class: 'RequesterRecipientProvider'], [$class: 'DevelopersRecipientProvider']],
                        subject: '${JOB_NAME}- Build # ${BUILD_NUMBER} - Failure!'
            }
        }
    }

    stages {
        // 编译构建代码
        stage('Build') {
            steps {
                //拉取工程代码
                git branch: '${BRANCH_NAME}', credentialsId: '******-***-***-****-**********', url: 'git@172.17.1.xxx:X/xxx/zc-ht/datamgt.git'

                container('jnlp-slave') {
                    script {
                        if ("$BRANCH_NAME" != 'master') {
                            sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -Dmaven.test.failure.ignore=true -f ${pomPath} clean install -Dautoconfig.skip=true -Pcoverage-per-test"
                        } else {
                            sh "mvn -f ${pomPath} clean install -Dmaven.test.skip=true"
                            //sh "mvn -f ${pomPath} clean install -Dtest -DfailIfNoTests=false"
                        }
                    }
                }
            }

        }
        //单元测试
        stage('Unit test') {
            steps {
                echo "starting unitTest......"
                //注入jacoco插件配置,clean test执行单元测试代码. All tests should pass.
                //junit '**/target/surefire-reports/*.xml'
                //配置单元测试覆盖率要求，未达到要求pipeline将会fail,code coverage.LineCoverage>20%.
                //jacoco changeBuildStatus: true, maximumLineCoverage:"20%"
            }
        }
        //静态检查
        stage('SonarQube') {
            steps {
                echo "starting codeAnalyze with SonarQube......"
                //sonar:sonar.QualityGate should pass
                container('jnlp-slave') {
                    withSonarQubeEnv('Sonar-6.7') {
                        //固定使用项目根目录${basedir}下的pom.xml进行代码检查
                        //sh "mvn -f pom.xml clean compile sonar:sonar"
                        sh "mvn sonar:sonar " +
                                "-Dsonar.sourceEncoding=UTF-8 "
                    }
                    script {
                        //  未通过代码检查，中断
                        timeout(10) {
                            //利用sonar webhook功能通知pipeline代码检测结果，未通过质量阈，pipeline将会fail
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                error "未通过Sonarqube的代码质量阈检查，请及时修改！failure: ${qg.status}"
                            }
                        }
                    }
                }
            }
        }
        //Make Branch Image
        stage('Make Branch Image') {
            when { not { branch 'master' } }
            steps {
                echo "starting Make Branch Image ......"
                //构建项目镜像文件，启动对应项目的k8s服务
                container('jnlp-slave') {
                    script {
                        // 获取分支对应端口
                        testport = sh(returnStdout: true, script: 'echo "`cat docker_shell/port.list|grep $BRANCH_NAME|awk \'{print \$2}\'`"').trim()
                        // 端口未获取到
                        if (testport == '') {
                            echo "分支未分配端口"
                            error "您的分支未分配端口，请联系项目经理！failure: 端口未分配"
                        }
                    }
                    sh """
			cp -r target/distribution docker_shell
                        sed -i "s#https://IP#http://${TESTIP}:${testport}#g" docker_shell/distribution/serverIp.properties
			chmod 755 docker_shell/*.sh
			mkdir /home/jenkins/.kube/
			cp docker_shell/config /home/jenkins/.kube/
			docker_shell/mkdocker.sh ${testport} ${BUILD_NUMBER} ${JOB_NAME}
			"""
                }
            }
            post {
                //失败执行
                failure {
                    echo "分支${BRANCH_NAME}删除服务......"
                    container('jnlp-slave') {
                        sh """
                        chmod 755 docker_shell/*.sh
                        docker_shell/always.sh ${JOB_NAME} ${BUILD_NUMBER}
                        """
                    }
                }
            }
        }
        //Make Master Image
        stage('Make Master Image') {
            when { branch 'master' }
            steps {
                echo "starting Make Master Image ......"
                //构建项目镜像文件，启动对应项目的k8s服务
                container('jnlp-slave') {
                    script {
                        // 获取分支对应端口
                        testport = sh(returnStdout: true, script: 'echo "`cat docker_shell/port.list|grep $BRANCH_NAME|awk \'{print \$2}\'`"').trim()
                        // 端口未获取到
                        if (testport == '') {
                            echo "分支未分配端口"
                            error "您的分支未分配端口，请联系项目经理！failure: 端口未分配"
                        }
                    }
                    sh """
			cp -r target/distribution docker_shell
        		sed -i "s#dev#test#g" docker_shell/distribution/application.yml
        		sed -i "s#cbdnews36#znz_2019#g" docker_shell/distribution/elas.properties
			chmod 755 docker_shell/*.sh
			mkdir /home/jenkins/.kube/
			cp docker_shell/config /home/jenkins/.kube/
			docker_shell/mmkdocker.sh ${testport} ${BUILD_NUMBER} ${JOB_NAME}
			"""
                }
            }
            post {
                //失败执行
                failure {
                    echo "主干${BRANCH_NAME}删除服务......"
                    container('jnlp-slave') {
                        sh """
                        chmod 755 docker_shell/*.sh
                        docker_shell/always.sh ${JOB_NAME} ${BUILD_NUMBER}
                        """
                    }
                }
            }
        }
        //自动化接口测试
        stage('Branch API test') {
            when { not { branch 'master' } }
            steps {
                echo "starting Branch API test ......"
                container('jnlp-slave') {
                    //根据服务名跑对应名称的脚本
                    script {
                        def input_result = input message: '请输入提交的服务名', ok: ' 确定', parameters: [string(defaultValue: '', description: '输入提交的服务名:', name: 'ServiceName')]
                        if (input_result == '') {
                            echo " 请输入可用的服务名！"
                            return false
                        }
                        // 服务名
                        ServiceName = input_result
                    }
                    sh """
                        sudo cp docker_shell/mysql-connector-java-5.1.47.jar  /usr/local/apache-jmeter-5.0/lib
        		sleep 10s
                        jmeter -Jprotocol=http -Jip=${TESTIP} -Jport=${testport} -n -t jmeter/${ServiceName}.jmx -l jmeter/${ServiceName}.jtl -e -o jmeter/ResultReport
        		"""
                    perfReport sourceDataFiles: "jmeter/${ServiceName}.jtl", errorFailedThreshold: 0
                }
            }
            post {
                //失败执行
                failure {
                    echo "分支${BRANCH_NAME}删除服务......"
                    container('jnlp-slave') {
                        sh """
                        docker_shell/always.sh ${JOB_NAME} ${BUILD_NUMBER}
                        """
                    }
                }
            }
        }
        //自动化接口测试
        stage('Master API test') {
            when { branch 'master' }
            steps {
                echo "starting Master API test ......"
                container('jnlp-slave') {
                    sh """
                        sudo cp docker_shell/mysql-connector-java-5.1.47.jar  /usr/local/apache-jmeter-5.0/lib
        		sed -i "s#/znz#/znz_2019#g" jmeter/${JMETERNAME}.jmx
                        sleep 10s
                        jmeter -Jprotocol=http -Jip=${TESTIP} -Jport=${testport} -n -t jmeter/${JMETERNAME}.jmx -l jmeter/${JMETERNAME}.jtl -e -o jmeter/ResultReport
        		"""
                    perfReport sourceDataFiles: "jmeter/${JMETERNAME}.jtl", errorFailedThreshold: 0
                }
            }
        }
        // 归档
        stage('Archive Artifacts') {
            steps {
                // 归档文件
                archiveArtifacts "${responseData}"
            }
        }
        //开发分支手工调试
        stage('UT test') {
            when { not { branch 'master' } }
            steps {
                echo "I am branch ......"
                script {
                    emailext(
                            subject: "PineLine '${JOB_NAME}' (${BUILD_NUMBER})开发分支，手工测试通知",
                            body: "提交的PineLine '${JOB_NAME}' (${BUILD_NUMBER})可以手工测试了\n请测试完毕后结束本次JOB!!!",
                            recipientProviders: [[$class: 'RequesterRecipientProvider'], [$class: 'DevelopersRecipientProvider']]
                    )
                    input message: "请前往以下地址测试\nhttp://${TESTIP}:${testport} \n测试通过请确认", ok: ' 确认通过'
                }
            }
            post {
                //始终执行
                always {
                    echo "分支${BRANCH_NAME}删除服务......"
                    container('jnlp-slave') {
                        sh """
                        docker_shell/always.sh ${JOB_NAME} ${BUILD_NUMBER}
                        """
                    }
                }
            }
        }
        //Push Image
        stage('Push Image') {
            when { branch 'master' }
            steps {
                echo "starting Push Image ......"
                //根据param.server分割获取参数,包括IP,username,password
                container('jnlp-slave') {
                    timeout(5) {
                        script {
                            emailext(
                                    subject: "PineLine '${JOB_NAME}' (${BUILD_NUMBER})对应的接口版本录入通知",
                                    body: "提交的PineLine '${JOB_NAME}' (${BUILD_NUMBER})需要输入接口版本号\n请及时前往输入${env.BUILD_URL}进行测试验收",
                                    recipientProviders: [[$class: 'RequesterRecipientProvider'], [$class: 'DevelopersRecipientProvider']]
                            )
                            def input_result = input message: '请输入Tag', ok: ' 确定', parameters: [string(defaultValue: '', description: '请输入Tag:', name: 'TAG')]
                            //echo "you input is "+input_result +",to do sth"
                            if (input_result == '') {
                                echo " 未输入版本号 ！！"
                                return false
                            }
                            // TAG标签号
                            gittag = input_result
                        }
                    }
                    withCredentials([usernamePassword(credentialsId: '******-***-***-****-**********', passwordVariable: GIT_PASSWORD, usernameVariable: 'GIT_USERNAME')]) {
                        sh """
			echo ${gittag}
			git config --global user.email "jenkins@xxxxxx.com"
			git config --global user.name "jenkins"
			git tag -a ${gittag} -m 'Release version ${gittag}'
			git push http://${GIT_USERNAME}:${GIT_PASSWORD}@172.17.1.xxx/X/znz/zc-ht/datamgt.git --tags
			echo 'git标签添加成功'
			docker_shell/pushimage.sh ${gittag} ${BUILD_NUMBER} ${JOB_NAME}
			"""
                    }
                }
            }
        }
    }
}