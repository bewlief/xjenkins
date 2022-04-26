    // https://github.service.anz/dsso/shareholder-annual-report-2019/blob/master/Jenkinsfile
    stage('Zip Sourcecode') {
      steps {
        sh "make bundle"
      }
    }
    stage('Push to Artifactory') {
      steps {
        script {
          def version = sh(returnStdout: true, script: "git describe --tags").trim()
          artifactory.publish("shareholder-annual-report-2019/website/${version}/shareholder-annual-report-2019-${version}.zip", "*.zip")
        }
      }
    }