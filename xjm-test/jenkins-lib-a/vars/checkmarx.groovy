/**
 * Executing Checkmarx Jenkins Plugin to request a Scan.
 * refer to https://www.jenkins.io/doc/pipeline/steps/checkmarx/
 *
 * @param projectName A full absolute name of a project.
 * @param checkmarxJenkinsTeamId Jenkins team id for the project.
 * refer to https://dcxcheckmarx.service.xhoe/CxWebClient/TeamsManagement.aspx?id=e362d31e-acaf-455b-b43e-2753d7a0a2e1
 */


def scan() {
  def checkmarxTeamId = "e362d31e-acaf-455b-b43e-2753d7a0a2e1"

  step([
    $class: 'CxScanBuilder',
    credentialsId: 'XOPS-jenkins-checkmarx',
    groupId: checkmarxTeamId,
    projectName: env.BUILD_TAG,
    comment: '${BUILD_URL} SHA${GIT_COMMIT}',
    filterPattern: '!**/.git/**/*, !Jenkinsfile',
    excludeFolders: '',
    excludeOpenSourceFolders: '',
    exclusionsSetting: 'job',
    fullScanCycle: 10,
    fullScansScheduled: true,
    generatePdfReport: true,
    includeOpenSourceFolders: '',
    osaEnabled: false,
    preset: '36',
    sourceEncoding: '1',
    waitForResultsEnabled: true,
    vulnerabilityThresholdEnabled: true,
    vulnerabilityThresholdResult: 'UNSTABLE',
    highThreshold: 1,
  ])
}
