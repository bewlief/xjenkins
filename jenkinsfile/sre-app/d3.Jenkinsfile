pipeline {
//    agent {
//        kubernetes {
//            label "alpine-zxcv"
//            defaultContainer 'jnlp'
//        }
//    }
    agent any


    options {
        disableConcurrentBuilds()

    }

    stages {
        stage("stage 1") {
            agent {
                docker { image "maven:3.6.3-jdk-8" }
            }
            steps {
                echo "stage 1, step 1"
                sh "mvn --version"
            }
        }

        stage("stage 2") {
            steps {
                echo "stage 2, step 1"
            }
        }
    }
}