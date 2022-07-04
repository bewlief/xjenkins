#!groovy
def oper_checkout(SCM_TYPE,SCM_URL,SCM_AUTH_PASS,SCM_BRANCH=' ',SCM_AUTH_USER=' '){
    if (SCM_TYPE =='git') {
      return checkout([$class: "GitSCM", branches: [[name: "${SCM_BRANCH}"]], \
                     doGenerateSubmoduleConfigurations: false, extensions: [], \
                      submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${SCM_AUTH_PASS}", \
                      url: "${SCM_URL}"]]])
    }
    else if (SCM_TYPE == 'tfs' ) {
      // need SCM_AUTH_USER args
      return checkout(changelog: false, poll: false, scm: [$class: 'TeamFoundationServerScm', \
                     password: new hudson.util.Secret("$SCM_AUTH_PASS"), projectPath: "${SCM_BRANCH}", \
                     serverUrl: "${SCM_URL}", useOverwrite: true, useUpdate: true, userName: "$SCM_AUTH_USER", \
                     workspaceName: 'Hudson-${JOB_NAME}'])              
    } 
    else if (SCM_TYPE == 'svn' ){
      return checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', \
               excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', \
               locations: [[cancelProcessOnExternalsFail: true, credentialsId: "${SCM_AUTH_PASS}",\
                depthOption: 'infinity', ignoreExternalsOption: true, local: '.', \
                remote: "${SCM_URL}" ]], \
                 quietOperation: true, workspaceUpdater: [$class: 'CheckoutUpdater']])    
    }
}
pipeline {
      
      environment {
      
        // define output packagename
        sourcefile="project_name"            
        //Tomcat Home Path
                
        SCM_TYPE = 'git'
        SCM_URL = 'gitutl'
        SCM_AUTH_PASS = 'jenkins_auth'
        SCM_BRANCH = 'dev'
        SCM_AUTH_USER = ' '
        
        // build cmd
        BUILD_CMD = 'mvn clean package'
        
        // define app env
        
        START_CMD = '/opt/apache-tomcat-8.5.34/bin/startup.sh'
        STOP_CMD = '/opt/apache-tomcat-8.5.34/bin/shutdown.sh'
        
        APP_PATH = '/opt/apache-tomcat-8.5.34/webapps/ROOT'    
        
        // Not need often change                
        zipfile="${sourcefile}.zip"
        outfile="${sourcefile}.war"
        
    
        }
      tools {
          maven 'maven3'   
          jdk 'jdk-8'
      }
      agent { label 'master' }
      
      options {
          buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30' ))
      }
    stages{
        stage('checkout'){
          steps{                       
              oper_checkout(SCM_TYPE,SCM_URL,SCM_AUTH_PASS,SCM_BRANCH,SCM_AUTH_USER)        
            }
        }
        stage('build'){
            steps{
                sh """                                                    
                ${BUILD_CMD};                
                """    
                // sh """
                
                // find target -name '*.war' | xargs -i mv {} target/${sourcefile}-1.0.jar
                // """
                script{
                  rvfind = sh returnStdout: true,script: "find target -name '${sourcefile}*.war'"
                  
                  if (rvfind == ''){
                     outfile = "${sourcefile}.jar"    
                     rvfind = sh returnStdout: true, script: "find target -name '${sourcefile}*.jar'"                             
                  }
                  
                  rvfind = rvfind.trim()    
                  sh """
                    if test ! -d ${sourcefile};then mkdir ${sourcefile};else rm -rf ${sourcefile}/* ; fi;
                    """
                  if (outfile.contains('war')){    
                      sh """
                       unzip -q ${rvfind} -d ${sourcefile};
                       """                       
                    } else {
                      sh " cp ${rvfind} ${sourcefile}/;"
                    }
                  sh ""                    
                }
                                
                script {
                    if (outfile.contains('war')){
                        if (APP_PATH.contains('ROOT')) {
                          sh """
                            cd ${sourcefile};
                            zip -qry $zipfile .;
                            cd .. && mv ${sourcefile}/$zipfile .;
                            """ 
                        }else{
                           sh """
                            zip -qry $zipfile ${sourcefile};
                           """
                        }    
                    } else {
                       sh """
                            cd ${sourcefile};
                            zip -qry $zipfile .;
                            cd .. && mv ${sourcefile}/$zipfile .;
                            """ 
                    }    
                }                
                stash includes: "$zipfile", name: "$zipfile"
            }
        }
        stage('service oper'){
            agent { label '192.168.8.105'}
            steps{
                sh 'rm -rf ./*'
                unstash "$zipfile"                   
                sh '''
                    echo "Normal Stop Process ...";
                    sudo su -c "$STOP_CMD"  && echo 'Process Stop Success' || echo 'Process Stop Fariure'                  
                    '''                                                                  
                script {
                   if (APP_PATH.contains('ROOT')) {
                            sh '''                                                          
                                 sudo su -c "if test ! -d $APP_PATH;then mkdir -p $APP_PATH;fi; cd ${APP_PATH};rm -rf ./*;unzip $WORKSPACE/$zipfile;";
                            '''        
                    } else {
                            sh '''                            
                               sudo su -c "if test ! -d $APP_PATH;then mkdir -p $APP_PATH;fi; cd ${APP_PATH};rm -rf ./${sourcefile}*;unzip $WORKSPACE/$zipfile;";                                 
                            '''                
                    }                        
                }                
                 sh '''
                    echo "Normal Start Process ...";
                    sudo su -c "$START_CMD"  && echo 'Process Start Success' || echo 'Process Start Fariure'                  
                    '''                                                                                          
            }
        }        
    }
}
