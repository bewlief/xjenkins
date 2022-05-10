pipeline {
  agent {
    kubernetes {
      label "k8s-devops-incapsula-testing"
      inheritFrom 'alpine'
      cloud  "openshift"
      namespace "XOPS-jenkins"
      yaml """
spec:
  securityContext:
    runAsUser: 0
  containers:
  - name: python
    image: hub.artifactory.gcp.xhoe/python:alpine
    command:
    - cat
    tty: true
"""
    }
  }

  options {
    timeout(time: 2, unit: 'HOURS')
    disableConcurrentBuilds()
    buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '7', numToKeepStr: '50')
  }

  parameters {
    string defaultValue: 'www.xhoe.com', description: 'FQDN of host to be tested', name: 'FQDN', trim: true
  }

  stages {
    stage('run test') {
      steps {
        container('python') {
          sh '''
            env | sort
            cp pip.conf /etc
            python -m pip install -U tox
            export ORG=${FQDN##*.}
            tox
          '''
        }
      }
    }
  }

  post{
    always{
      junit allowEmptyResults: true, testResults: 'junit.xml'
    }
  }
}
