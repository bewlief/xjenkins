
# s4的基础上，进一步设置auhentication

FROM jenkins/jenkins:latest
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/jenkins_home/s5.yaml

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins.yaml /var/jenkins_home/s5.yaml
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
