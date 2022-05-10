pipeline {
    agent { label "alpine" }

    parameters {
//        choice(
//                choices: 'release/1.17.0\nfeature/amsdev\nmaster',
//                description: 'select your branch',
//                name: 'branch')
        string(defaultValue: "master",
                description: 'Choose the branch which you want to scan',
                name: 'branch')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

    stages {
        // 其实下面这个stage是不需要的，对当前的jenkins而言，每一个job已经自动和相应的repo/branch做了绑定，会自动下载该branch的代码
        // 除非是单独创建的job，才需要手动下载代码
        // 且需要注意，XOPS-jenkins-ssh是不支持https的endpoint的，必须使用git@...的endpoint
        stage("checkout content-infographics") {
            steps {
                script {
                    checkout([
                            $class           : 'GitSCM',
                            branches         : [[name: "${params.branch}"]],
                            extensions       : [],
                            submoduleCfg     : [],
                            userRemoteConfigs: [
                                    [credentialsId: 'XOPS-jenkins-ssh', url: "git@github.service.xhoe:dsso/content-infographics.git"]
                            ]
                    ]
                    )
                }

            }
        }


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
