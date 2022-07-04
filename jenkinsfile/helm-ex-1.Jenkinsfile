
def label = "slave-${UUID.randomUUID().toString()}"
 
def helmLint(String chartDir) {
    println "校验 chart 模板"
    sh "helm lint ${chartDir}"
}
 
def helmInit() {
    println "初始化 helm client"
    sh "helm init --client-only --stable-repo-url https://mirror.azure.cn/kubernetes/charts/"
}
 
def helmRepo(Map args) {
    println "添加 myrepo repo"
    sh "helm repo add --username ${args.username} --password ${args.password} myrepo http://k8s.harbor.test.site/chartrepo/system"
 
    println "update repo"
    sh "helm repo update"
 
    println "fetch chart package"
    sh """
      helm fetch myrepo/polling
      tar xzvf polling-0.1.0.tgz
    """
}
 
def helmDeploy(Map args) {
    helmInit()
    helmRepo(args)
 
    if (args.dry_run) {
        println "Debug 应用"
    sh "helm upgrade --dry-run --debug --install ${args.name} ${args.chartDir} --set persistence.persistentVolumeClaim.database.storageClass=dynamic --set database.type=external --set database.external.database=polling_app --set database.external.username=polling --set database.external.password=polling321 --set api.image.repository=${args.image} --set api.image.tag=${args.tag} --set imagePullSecrets[0].name=myreg --namespace=${args.namespace}"
    } else {
        println "部署应用"
    sh "helm upgrade --install ${args.name} ${args.chartDir} --set persistence.persistentVolumeClaim.database.storageClass=dynamic --set database.type=external --set database.external.database=polling_app --set database.external.username=polling --set database.external.password=polling321 --set api.image.repository=${args.image} --set api.image.tag=${args.tag} --set imagePullSecrets[0].name=myreg --namespace=${args.namespace}"
        echo "应用 ${args.name} 部署成功. 可以使用 helm status ${args.name} 查看应用状态"
    }
}
 
podTemplate(label: label, containers: [
  containerTemplate(name: 'maven', image: 'maven:3.6-alpine', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'kubectl', image: 'cnych/kubectl', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'cnych/helm', command: 'cat', ttyEnabled: true)
], volumes: [
  hostPathVolume(mountPath: '/root/.m2', hostPath: '/var/run/m2'),
  hostPathVolume(mountPath: '/home/jenkins/.kube', hostPath: '/root/.kube'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def gitBranch = myRepo.GIT_BRANCH
 
    def imageTag = sh(script: "git rev-parse --short HEAD",returnStdout:true).trim()
    def dockerRegistryUrl = "k8s.harbor.test.site"
    def imageEndpoint = "system/polling-app-server"
    def image = "${dockerRegistryUrl}/${imageEndpoint}"
 
    stage('单元测试') {
     input id: 'ncpprd', message: '发布生产请找-张三--批准?', ok: '确认', submitter: 'admin'
     echo "1.测试阶段"
    }
    stage('代码编译打包') {
     try {
        container('maven') {
          echo "2. 代码编译打包阶段"
          sh "cd polling-app-server && mvn clean package -Dmaven.test.skip=true"
      }
    }   catch (exc) {
        println "构建失败 - ${currentBuild.fullDisplayName}"
        throw (exc)
    }
  }
    stage('构建 Docker 镜像') {
      withCredentials ([[$class: 'UsernamePasswordMultiBinding',
        credentialsId: 'k8sharbor',
        usernameVariable: 'DOCKER_HUB_USER',
        passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          container('docker') {
           echo "3. 构建 Docker 镜像阶段"
           sh """
              docker login ${dockerRegistryUrl} -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
              cd polling-app-server && docker build -t ${image}:${imageTag} .
              docker push ${image}:${imageTag}
          """
      }
  }
}
 
    // stage('运行 Kubectl') {
    // container('kubectl') {
    //    echo "查看 K8S 集群 Pod 列表"
    //    sh "kubectl get pods"
    //    sh """
    //       sed -i "s#<IMAGE>#${image}#g" polling-app-server/polling-app-server.yaml
    //       sed -i "s#<IMAGE_TAG>#${imageTag}#g" polling-app-server/polling-app-server.yaml
    //       kubectl apply -f polling-app-server/polling-app-server.yaml
    //    """
    //  }
    //}
 
    stage('运行 Helm') {
       withCredentials([[$class: 'UsernamePasswordMultiBinding',
       credentialsId: 'k8sharbor',
       usernameVariable: 'DOCKER_HUB_USER',
       passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
         container('helm') {
        // todo，也可以做一些其他的分支判断是否要直接部署
        echo "4. [INFO] 开始 Helm 部署"
        helmDeploy(
            dry_run     : false,
            name        : "polling",
            chartDir    : "polling",
            namespace   : "prd",
            tag         : "${imageTag}",
            image       : "${image}",
            username    : "${DOCKER_HUB_USER}",
            password    : "${DOCKER_HUB_PASSWORD}"
 
        )
        echo "[INFO] Helm 部署应用成功..."
       }
     }
   }
 }
}