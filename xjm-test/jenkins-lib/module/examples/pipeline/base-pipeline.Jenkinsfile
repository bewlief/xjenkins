package pipeline

//自定义mysh，屏蔽sh输出调试信息
def mysh(cmd, returnStatus) {
    return sh(script: '#!/bin/sh -e\n' + cmd, returnStatus: returnStatus)
}

pipeline {
    agent any

    parameters {
        string(name: 'branch', defaultValue: 'master', description: 'Branch Name')
        string(name: 'module', defaultValue: 'test', description: 'Module Name')
    }
    environment {
        branch = "$params.branch"
        module = "$params.module"
    }

    stages {
        stage('Build Parameters') {
            steps {
                mysh('echo "You will used branch $branch."', true)
                mysh('echo "You will used branch $module."', true)
            }
        }
    }
}
