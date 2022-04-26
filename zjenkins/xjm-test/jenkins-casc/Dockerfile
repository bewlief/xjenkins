FROM jenkins-zh:lts

RUN mkdir $JENKINS_HOME/casc_configs
#COPY ./jenkins.yaml $JENKINS_HOME/casc_configs/jenkins.yaml
COPY ./*.yaml $JENKINS_HOME/casc_configs/
ENV CASC_JENKINS_CONFIG=$JENKINS_HOME/casc_configs

ENV JAVA_HOME=/usr/local/jdk
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

ADD ./maven-3.6.3.tar maven-3.6.3.tar
ADD ./terraform terraform
ENV MAVEN_HOME /maven-3.6.3.tar/maven-3.6.3
ENV PATH $PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin

ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false

RUN install-plugins.sh \
  configuration-as-code \
  configuration-as-code-support \
  blueocean \
  job-dsl \
  cloudbees-folder \
  workflow-aggregator \
  pipeline-utility-steps \
  credentials \
  generic-webhook-trigger \
  git-changelog \
  violation-comments-to-gitlab \
  terraform \
  ansiColor \
  http_request \
  workflow-cps-global-lib-http