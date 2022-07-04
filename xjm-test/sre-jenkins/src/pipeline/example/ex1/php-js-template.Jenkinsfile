// php+js项目

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

node {
    def repoUrl = 'git@******.git'
    def repoBranch = 'develop'
    def imageName = '***/code'
    def imageTag = 'dev'
    def buildEnv = 'develop'

    // rancher2.X部署所需变量
    def rancherUrl = 'https://<rancher2_service>/v3/project/<cluster_id>:<project_id>'
    def rancherNamespace = ''
    def rancherService = ''
    def recipients = ''
    def jenkinsHost = ''    // jenkins服务器地址

    // 认证ID
    def gitCredentialsId = 'aa651463-c335-4488-8ff0-b82b87e11c59'
    def settingsConfigId = '3ae4512e-8380-4044-9039-2b60631210fe'
    def rancherAPIKey = 'd41150be-4032-4a53-be12-3024c6eb4204'
    def soundsId = '69c344f1-8b11-47a1-a3b6-dfa423b94d78'

    env.SONAR_HOME = "${tool 'sonarscanner'}"
    env.PATH="${env.SONAR_HOME}/bin:${env.PATH}"

    try {
        stage("prepare") {
            // 拉取需要构建的代码
            git_maps = checkout scm: [$class: 'GitSCM', branches: [[name: "*/${repoBranch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], userRemoteConfigs: [[credentialsId: "${gitCredentialsId}", url: "${repoUrl}"]]]
            env.GIT_REVISION = git_maps.GIT_COMMIT[0..8]
        }
        stage('CodeCheck'){
            // sonar_scanner进行代码静态检查
            withSonarQubeEnv('sonar') {
                sh """
                sonar-scanner -X \
                -Dsonar.projectKey=$JOB_NAME \
                -Dsonar.projectName=$JOB_NAME \
                -Dsonar.projectVersion=$GIT_REVISION \
                -Dsonar.sourceEncoding=UTF-8 \
                -Dsonar.modules=php-module,javascript-module \
                -Dphp-module.sonar.projectName=PHP-Module \
                -Dphp-module.sonar.language=php \
                -Dphp-module.sonar.sources=. \
                -Dphp-module.sonar.projectBaseDir=backend/app \
                -Djavascript-module.sonar.projectName=JavaScript-Module \
                -Djavascript-module.sonar.language=js \
                -Djavascript-module.sonar.sources=. \
                -Djavascript-module.sonar.projectBaseDir=front/src
                """
            }
        }
        stage("QualityGate") {
            // 获取sonar检查结果
            timeout(time: 1, unit: "HOURS") {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        }
        stage("frontBuild") {
            // 前端构建
            docker.image("node").inside() {
                sh """
                cd front
                npm install
                npm run build ${buildEnv}
                """
            }
        }
        stage("back-build") {
            // 后端构建
            docker.image("<docker_registry>/composer:v0").inside() {
                sh """
                cd backend
                composer install
                """
            }
        }
        stage("docker-code-build") {
            // 代码镜像构建
            sh "cp -r front/build docker/code/build"
            sh "cp -r backend docker/code/api"
            dir("docker/code") {
                docker.build("${imageName}:${imageTag}").push()
                sh "docker rmi ${imageName}:${imageTag}"
            }
        }
        stage('rancherDeploy') {
            // Rancher上更新容器
            // 查询容器名称
            def response = httpRequest acceptType: 'APPLICATION_JSON', authentication: "${rancherAPIKey}", contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN', timeout: 10, url: "${rancherUrl}/pods/?workloadId=deployment:${rancherNamespace}:${rancherService}"
            def podsInfo = new JsonSlurperClassic().parseText(response.content)
            def containerName = podsInfo.data[0].name
            print(containerName)
            response.close()

            // 删除容器
            httpRequest acceptType: 'APPLICATION_JSON', authentication: "${rancherAPIKey}", contentType: 'APPLICATION_JSON', httpMode: 'DELETE', responseHandle: 'NONE', timeout: 10, url: "${rancherUrl}/pods/${rancherNamespace}:${containerName}"
        }
        stage("tear-down") {
            sh "rm -rf docker"
        }
        httpRequest authentication:"${soundsId}", url:'http://${jenkinsHost}/sounds/playSound?src=file:///var/jenkins_home/sounds/4579.wav'
        emailext attachlog:true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT', to: "${recipients}"
    } catch(err) {
        currentBuild.result = 'FAILURE'
        httpRequest authentication:"${soundsId}", url:'http://${jenkinsHost}/sounds/playSound?src=file:///var/jenkins_home/sounds/8923.wav'
        emailext attachlog:true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT', to: "${recipients}"
    }
}