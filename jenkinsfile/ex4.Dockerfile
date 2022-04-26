FROM hub.artifactory.gcp.anz/python:3.7.4-alpine as deps

LABEL MAINTAINER audevops@anz.com

ARG aopdemo.proxy=
ARG http_proxy=$aopdemo.proxy
ARG https_proxy=$aopdemo.proxy
ARG no_proxy=.anz,.test
ARG openshift_user=1001380000

RUN printf 'https://artifactory.gcp.anz/artifactory/alpinelinux/alpine/v3.10/main\nhttps://artifactory.gcp.anz/artifactory/alpinelinux/alpine/v3.10/community' > /etc/apk/repositories

RUN apk update

RUN apk add --no-cache --update \
      alpine-sdk \
      curl \
      libffi-dev

# Set some permissions and ownership to openshift account
RUN echo "user:x:$openshift_user:${openshift_user}::/home/user:" >> /etc/passwd && \
    echo "user:!:$(($(date +%s) / 60 / 60 / 24)):0:99999:7:::" >> /etc/shadow && \
    echo "user:x:${openshift_user}:" >> /etc/group && \
    mkdir -p /home/user/ && chown user /home/user/ && chmod 775 /home/user && \
    mkdir -p /app && chown user /app && chmod 775 /app

#set workdir
WORKDIR /app

# copy files
COPY requirements /app/
COPY files/ /app/

# add our CA certs to system
RUN cat /app/ca_certs >> /etc/ssl/certs/ca-certificates.crt

USER user

# install requirements
ENV GRPC_PYTHON_BUILD_EXT_COMPILER_JOBS 16
RUN pip install --user -r requirements -i https://artifactory.gcp.anz/artifactory/api/pypi/pypi/simple

FROM deps

USER 0

# copy app files
COPY main.py /app/
COPY app/ /app/app
COPY tests/ /app/tests

RUN chgrp -Rf user /app && chown -R user /app && \
    chmod -Rf ug+wrxs /app

USER user

# set up environment variables for application
ENV PATH=$PATH:/home/user/.local/bin FLASK_ENV=development REQUESTS_CA_BUNDLE=/etc/ssl/certs/ca-certificates.crt

# RUN UNIT TESTS
RUN pytest -vv

EXPOSE 9999

# run app Server
CMD ["python", "main.py"]