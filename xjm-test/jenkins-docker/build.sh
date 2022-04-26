#!/bin/bash
# docker pull jenkins/jenkins:lts
docker rmi -f docker-jenkins-sandbox:lts && docker build . -t docker-jenkins-sandbox:lts
docker rm -f jenkins-sandbox
docker build . -t docker-jenkins-sandbox:lts
#docker tag docker-jenkins-sandbox:latest docker-jenkins-sandbox:lts
