#!groovy
package com.xhoe.ops

def call() {
    node {
        //Need to replace localhost with hostIp when working locally.
//        def hostIp = getHostIp(this)
        def hostIp = "localhost"

//        def jenkinsUrl = env.jenkinsUrl.replace('localhost', hostIp.trim())
        def jenkinsUrl = env.jenkinsUrl
//        def gitlabUrl = env.gitlabUrl.replace('localhost', hostIp.trim())
        def gitlabUrl = env.gitlabUrl

//        def restClient = new RestClient(this, gitlabUrl)

        stage('Checkout') {
            deleteDir()
            sh """
        echo "git clone finished"
      """
        }

        stage("Static check") {
            echo "static check now"
        }

        stage('mvn test') {
//            mvn - version
        }

        stage('Cleanup') {
            echo "clean up"
        }
        stage('package') {
            echo "package"
        }
        stage('deploy to dev') {
            echo "deploy to dev k8s"
        }
    }
}