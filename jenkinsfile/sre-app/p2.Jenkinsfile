podTemplate(containers: [
        containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'golang', image: 'golang:1.8.0', ttyEnabled: true, command: 'cat')
]) {

    node(POD_LABEL) {
        stage('Get a Maven project') {
            container('maven') {
                stage('Build a Maven project') {
                    echo "in container maven"
                    sh "sleep 30"
                }
            }
        }

        stage('Get a Golang project') {
            container('golang') {
                stage('Build a Go project') {
                    echo "in container golang"
                    sh "sleep 30"
                }
            }
        }

    }
}