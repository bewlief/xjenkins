def publish() {
    withCredentials([usernamePassword(
    credentialsId: 'gcp-artifactory-npm',
    usernameVariable: 'JENKINS_ARTIFACTORY_NPM_USERNAME',
    passwordVariable: 'JENKINS_ARTIFACTORY_NPM_PASSWORD')]) {
      sh 'npm publish'
    }
}
