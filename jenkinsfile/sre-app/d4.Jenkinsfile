// https://blog.csdn.net/nklinsirui/article/details/90403311

def label = "k8s-slave-${UUID.randomUUID().toString()}"
podTemplate(label: label, containers: [
        containerTemplate(
                // name=jnlp时，才会允许使用自定义的image替换默认的jnlp container template
                name: 'jnlp',
                image: 'jenkins/jnlp-slave:3.27-1-alpine',
                alwaysPullImage: false,
                privileged: true,
                args: '${computer.jnlpmac} ${computer.name}'),
        // 除了jnlp容器，其他容器必须使用 command: cat ttyEnabled:true老保证容器一直保持运行状态
        containerTemplate(name: 'maven', image: 'maven:3.6-jdk-8-alpine', command: 'cat', ttyEnabled: true, privileged: true),
        containerTemplate(name: 'docker', image: 'docker:18.06', command: 'cat', ttyEnabled: true, privileged: true),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.15', command: 'cat', ttyEnabled: true, privileged: true),
],
        namespace: 'devops', serviceAccount: 'jenkins', automountServiceAccountToken: 'true',
        volumes: [
                hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
                hostPathVolume(hostPath: '/root/.m2', mountPath: '/root/.m2')
        ]) {
    node(label) {
        stage('Test Docker') {
            container('docker') {
                sh 'docker version'
            }
        }
        stage('Test Maven') {
            container('maven') {
                sh 'mvn -v'
            }
        }
        stage('Test Kubernetes') {
            container('kubectl') {
                sh 'kubectl get pods --all-namespaces'
            }
        }
    }
}
