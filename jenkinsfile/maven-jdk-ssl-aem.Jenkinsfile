pipeline {
  agent {
    kubernetes {
      label "k8s-maven3.6.3-jdk8"
      inheritFrom "alpine"
      cloud  "openshift"
      namespace "XOPS-jenkins"
      yaml """
spec:
  containers:
  - name: k8s-maven3-jdk8
    image: hub.artifactory.gcp.xhoe/maven:3.6.3-jdk-8
    securityContext:
      runAsUser: 0
    command:
    - cat
    tty: true
"""
    }
  }

  stages {

    stage ('checkout source codes') {
            agent { docker 'maven:3-alpine' }
      steps {
        checkout(
            [
            $class                           : 'GitSCM',
            branches                         : [[name: "xinj-0225"]],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [],
            submoduleCfg                     : [],
            userRemoteConfigs                : [
                        [credentialsId: 'xinj', url: "https://github.service.xhoe/dsso/content-aem"]
                ]
            ]
        )
          sh """
           which java
           env
           id
           java -version
           # 使用自己导入证书后的cacerts
           cp -f ./devops/cacerts /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/
           ls -l /usr/lib/jvm/java-1.8-openjdk/jre/lib/security
           mvn clean package
          """
        }
    }
  }

}
