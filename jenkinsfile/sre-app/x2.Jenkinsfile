pipeline {
    agent {
        kubernetes {
            label "k8s-maven3.6.3-jdk8"
            inheritFrom "alpine"
            cloud "kubernetes"
            namespace "devops"
            yaml """
spec:
  containers:
  - name: k8s-maven3-jdk8
    image: maven:3.6.3-jdk-8
    command:
    - cat
    tty: true
"""
        }
    }

    stages {
        stage("checkout code") {
            steps {
                container("k8s-maven3-jdk8") {
                    echo "in conatiners"
                }
            }
        }
    }
}