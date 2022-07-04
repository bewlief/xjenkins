# Step 1: gradle build
FROM hub.artifactory.gcp.xhoe/gradle:6.8-jdk11 AS builder
WORKDIR /home/java
COPY . .

# this import key line might not needed, udeploy or openshif will do this
RUN gradle \
    -Djavax.net.ssl.trustStore=$JAVA_HOME/lib/security/cacerts \
    -Djavax.net.ssl.trustStorePassword=changeit \
    clean build --no-daemon
    # clean build -PenvName=local --no-daemon

# Step 2: artifact
FROM dtrprod.docker.service.xhoe/base-images/ubi8-openjdk-11

# default is root, explicit here
USER root

# Setup certs
COPY ./cacerts/global /etc/pki/ca-trust/source/anchors/
COPY ./cacerts/globaltest /etc/pki/ca-trust/source/anchors/
RUN update-ca-trust extract

# install tools for debug network traffic during project setup
COPY ubi-8.repo /etc/yum.repos.d/ubi.repo

# update to reduce number of vulnerabilities, yet added extra layer and duplicate vulnerabilities
# RUN microdnf update -y --nobest --nodocs && \
# temp install network tools for debug. only vim & less is needed
RUN microdnf install -y --nodocs vim-enhanced less bind-utils iputils iproute openssl net-tools && \
    microdnf clean all


# ubi image bundle with jboss user (185)
USER jboss
WORKDIR /home/jboss

# build time variables, with default value
ARG PORT=9090
ARG APP_PROP_FILE=ci
#ARG MB_URL
#ARG OCV_PORT
ARG JAVA_OPTS

EXPOSE $PORT

RUN mkdir -p /home/jboss/logs
RUN chmod a+rw /home/jboss/logs

COPY --from=builder /home/java/web/build/libs/colabff-*.jar /home/jboss/colabff.jar
COPY --from=builder /home/java/config/colabff-$APP_PROP_FILE.properties /home/jboss/colabff.properties

# TODO splunk, newRelic

ENTRYPOINT java $JAVA_OPTS -jar /home/jboss/colabff.jar \
     --colabff.config.file=file:/home/jboss/colabff.properties
