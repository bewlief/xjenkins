#!/bin/bash
export personal_access_token=$(cat ../gitlab-docker/setup/personal-access-token.txt)
echo --------------------------------------------------------
echo -
echo -  Using GitLab personal access token:
echo -
echo -    $personal_access_token
echo -
echo --------------------------------------------------------

docker run --rm --name jenkins-sandbox -p 8909:8080 docker-jenkins-sandbox:lts
