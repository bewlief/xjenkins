pipeline {
  agent any
  environment {
    cuiDir = 'cui'
    activecuiDir = 'activenet-cui'
    credentialsId = '2a26b18c-41f7-4b9f-b101-5482c89b0431'
    defaultBranch = 'develop'
  }
  parameters {
    string(name: 'cuiBranch', defaultValue: 'develop', description: 'cui branch')
    string(name: 'activenetcuiBranch', defaultValue: 'develop', description: 'activenet-cui branch')
    choice(name: 'tagType', choices: 'patch\nminor\nmajor', description: 'Create new tag')
  }
  stages {
    stage("Checkout") {
      steps {
        script {
          if (env.gitlabTargetBranch != "") {
            //params.cuiBranch = env.gitlabTargetBranch
            //params.activenetcuiBranch = env.gitlabTargetBranch
          }
          echo "params: ${params}"
          sh "printenv"
          sh "rm -rf ./${cuiDir}"
          sh "git clone git@gitlab.dev.activenetwork.com:ActiveNet/${cuiDir}.git"
          if ( params.cuiBranch != defaultBranch ) {
            dir("${cuiDir}") {
              sh "git checkout origin/${cuiBranch} -b ${cuiBranch}"
            }
          }
        }
      }
    }
    stage('Prep') {
      steps {
        dir("${cuiDir}"){
          sh '''
          yarn config set registry https://artifacts.dev.activenetwork.com/artifactory/api/npm/npmjs
          npm config set registry https://artifacts.dev.activenetwork.com/artifactory/api/npm/npmjs
          node -v
          mkdir -p ~/.npm-global
          npm config set prefix '~/.npm-global'
          echo ' export PATH=~/.npm-global/bin:$PATH' >> ~/.bash_profile
          source ~/.bash_profile
          npm install -g np
          yarn cache clean
          '''
        }
        retry(3) {
          dir("${cuiDir}"){
            sh 'yarn install'
          }
        }
      }
    }
    stage('Update Version and Tag') {
      steps {
          dir("${cuiDir}"){
            sh "git reset --hard origin/${params.cuiBranch}"
            sh "git tag | xargs git tag -d"
            sh "git pull origin ${params.cuiBranch}"
            sh "npm version ${params.tagType} -m 'Bump version to %s'"
            sh "git push origin ${params.cuiBranch}"
            sh "git push --tags"
            script {
              def props = readJSON file: 'package.json';
              echo props.version;
              def version = props.version;
              currentBuild.displayName = "$version"
              env.version = version
            }
          }
      }
    }
    stage('Build') {
      steps {
          dir("${cuiDir}"){
            sh "yarn build:prod:lib"
          }
      }
    }
    stage('Upload Artifact') {
      steps {
        dir("${cuiDir}"){
          sh '''
            releaseFolder=./release
            libFolder=./lib
            nexusBaseRepository=http://nexus.dev.activenetwork.com/nexus/service/local/repositories/libs-releases/content/com/active
            ui=activenet-cui-fee
            # Prepare to build
            rm -rf $releaseFolder
            mkdir $releaseFolder
            currentVersion=`git describe --tags --abbrev=0 | cut -c 2-`
            zipFileName=${ui}-${currentVersion}.zip
            zipFilePath=$releaseFolder/$zipFileName
            echo 'zip package...'
            cd $libFolder
            zip -r ./$zipFileName *
            cd ..
            mv $libFolder/$zipFileName ${releaseFolder}/$zipFileName
            echo 'back to project folder'
            echo 'zip package done'
            repository=${nexusBaseRepository}/$ui/${currentVersion}/$zipFileName
            curl -vf --upload-file $zipFilePath $repository
            rm -rf $releaseFolder
            echo 'Uploaded package'
          '''
        }
      }
    }
    stage('Update activenet-cui version') {
      steps {
        // dir("${cuiDir}") {
        //   script {
        //     def props = readJSON file: 'package.json';
        //     echo props.version;
        //     def version = props.version;
        //     currentBuild.displayName = "$version"
        //     env.version = version
        //   }
        // }
        sh "rm -rf ./${activecuiDir}"
        sh "git clone git@gitlab.dev.activenetwork.com:ActiveNet/${activecuiDir}.git"
        dir("${activecuiDir}"){
          script {
            if ("${activenetcuiBranch}" != defaultBranch) {
              sh "git checkout origin/${activenetcuiBranch} -b ${activenetcuiBranch}"
            }
          }
          sh "echo ${env.version} > .cuiversion"
          sh "git add .cuiversion"
          sh "git commit -m 'update cui version ${env.version}'"
          retry(3) {
            dir("${activecuiDir}"){
              sh "git push origin ${params.activenetcuiBranch}"
            }
          }
        }
      }
    }
  }
}
