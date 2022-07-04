/**
 **/
//必须要有package才能被其他groovy import进去
package devops.common

//调用src下的function，需要用def c=new devops.common.common(); c.checkoutGit("url")
//而调用vars下的，则直接调用即可：xjm()->xjm.call(), xjm.test()
def checkoutGit(repoUrl) {
    echo "checkoutGit: common.checkoutGit()"
    checkout([
            $class                           : 'GitSCM',
            branches                         : [[name: '*/test']],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [],
            submoduleCfg                     : [],
            userRemoteConfigs                : [
                    [
                            credentialsId: 'bewlief-gitee',
                            url          : 'https://gitee.com/bewlief/sre-app.git'
                    ]
            ]
    ]
    )

    sh """
        pwd
        ls -lR
    """
}

/**
 This method builds a war
 **/
def buildWar(javaHome, buildCmd) {
    sh "export JAVA_HOME=${javaHome} && ${buildCmd}"
}

return this
