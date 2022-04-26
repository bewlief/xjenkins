import devops.common.common

def call() {
    echo "hello xjm.groocy, xjm.call(), default function"
    new common().checkoutGit("https://gitee.com/bewlief/sre-app.git")
}

def test() {
    echo "this is xjm.test()"
    new devops.common.utils().PrintMes("color message", "red")
//    echo buildTag()
}

def sendEmail(to, subject, body, mimeType = "text/html") {
    echo "send email to $to"

//    def e = new devops.email.Email()
//    e.send(
//            subjectTag: "subject is here",
//            body: "body is here",
//            to: "to is here"
//    )
}

// git clone branch到指定目录下
def checkoutGitx(repoUrl, branch, targetPath = "") {
    echo "checkoutGit: common.checkoutGit()"
    echo "url= $repoUrl, branch=$branch, target=$targetPath"
    def exitCode = checkout([
            $class                           : 'GitSCM',
            branches                         : [[name: "*/dev"]],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [
                    [$class: 'RelativeTargetDirectory', relativeTargetDir: "${targetPath}"],
                    [$class: 'CheckoutOption', timeout: 5]
            ],
            submoduleCfg                     : [],
            userRemoteConfigs                : [
                    [
                            credentialsId: 'bewlief-gitee',
                            url          : "$repoUrl",
                    ]
            ]
    ])

    /**
     * exit code =[GIT_AUTHOR_EMAIL:dse-jenkins@anz.com,
     * GIT_AUTHOR_NAME:DSE-Jenkins,
     * GIT_BRANCH:origin/test, G
     * IT_COMMIT:f354a8e7f85ca5cca1e3d2f2c25c37813339c7c3,
     * GIT_COMMITTER_EMAIL:dse-jenkins@anz.com,
     * GIT_COMMITTER_NAME:DSE-Jenkins,
     * GIT_PREVIOUS_COMMIT:f354a8e7f85ca5cca1e3d2f2c25c37813339c7c3,
     * GIT_PREVIOUS_SUCCESSFUL_COMMIT:f354a8e7f85ca5cca1e3d2f2c25c37813339c7c3,
     * GIT_URL:https://gitee.com/bewlief/sre-app.git]
     */
    sh """
        echo "exit code =$exitCode"
        echo "GIT_AUTHOR_NAME=$exitCode.GIT_AUTHOR_NAME"
    """
}


def getBuildId(String prefix = "xjm") {
    return "${prefix}${env.JOB_NAME}_${env.BUILD_NUMBER}".replaceAll('[^A-Za-z0-9]', '_')
}