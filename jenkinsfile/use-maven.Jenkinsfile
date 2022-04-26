pipeline {
  agent {
    kubernetes {
      label "k8s-maven3.6.3-jdk8"
      inheritFrom "alpine"
      cloud  "openshift"
      namespace "dse-jenkins"
      yaml """
spec:
  containers:
  - name: k8s-maven3-jdk8
    image: hub.artifactory.gcp.anz/maven:3.6.3-jdk-8
    command:
    - cat
    tty: true
"""
    }
  }

  stages {

    stage ('checkout source codes') {
      steps {
        git 'https://github.service.anz/xinj/xops-test-app.git'
        container('k8s-maven3-jdk8') {
          sh """
            mvn clean package --settings ./settings.xml
          """
        }
      }
    }
  }

}