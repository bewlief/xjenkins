pipeline {
  agent { label 'nodejs' }
  stages {
    stage('Checkmarx Code Scan') {
      when {
        anyOf {
          branch 'master'
          branch 'release/*'
        }
      }
      steps {
        script {
          checkmarx.scan([
            // extend parameter in dsso/jenkins-lib/var/checkmarx.groovy
            filters: [
              "!**/.git/**/*",
              "!Jenkinsfile",

              // UI Test files
              "!ui/src/**/*.test.ts",
              "!ui/src/**/*.test.tsx",

              "!bff/**/*_test.go", // BFF Test files
              "!bff/vendor/**/*", // BFF Vendors

              "!tools/**/*",  // deployment
              "!e2e/**/*"     // end to end testing
            ]
          ])
        }
      }
    }
  }
}
