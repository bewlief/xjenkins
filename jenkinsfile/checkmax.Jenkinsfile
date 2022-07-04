pipeline {

  agent { label 'nodejs' }
  stages {
    stage('Checkmarx Code Scan') {

      steps {
        script {
          checkmarx.scan([
            filter: [
              "!**/.git/**/*",
              "!Jenkinsfile",

              // UI Test files
              "!ui/src/**/*.test.ts",
              "!ui/src/**/*.test.tsx",

              "!bff/**/*_test.go", // BFF Test files
              "!bff/vendor/**/*", // BFF Vendors
            ]
          ])
        }
      }
    }
  }
}


