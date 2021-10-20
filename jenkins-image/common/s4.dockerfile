
# s3的基础上，进一步自动化配置Jenkins:
# 创建一个管理用户用于登录

FROM jenkins/jenkins:latest
#FROM jenkinszh/jenkins-zh

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/jenkins_home/s4.yaml

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins.yaml /var/jenkins_home/s4.yaml
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
