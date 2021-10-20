
# s2的基础上，增加了CASC_JENKINS_CONFIG，去自动化配置jenkins
# 现在只是配置 server_ip

FROM jenkins/jenkins:latest
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/jenkins_home/s3.yaml

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins.yaml /var/jenkins_home/s3.yaml
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
