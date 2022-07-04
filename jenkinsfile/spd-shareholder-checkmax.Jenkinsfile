pipeline {
  agent { label 'nodejs' }
  stages {
    stage('Checkmarx Code Scan') {
      steps {
        script {
          checkmarx.scan()
        }
      }
    }
    stage('Zip Sourcecode') {
      steps {
        sh "make bundle"
      }
    }
    stage('Push to Artifactory') {
      steps {
        script {
          def version = sh(returnStdout: true, script: "git describe --tags").trim()
          artifactory.publish("shareholder-annual-report-2020/website/${version}/shareholder-annual-report-2020-${version}.zip", "*.zip")
        }
      }
    }
  }
}
