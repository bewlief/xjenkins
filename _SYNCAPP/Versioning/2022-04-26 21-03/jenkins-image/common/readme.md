## 讲解如何在官方的jenkins image基础上定制自己的jenkins image

+ 官方的Jenkins image： jenkins/jenkins:lts：
+ 功能简介：

## 　使用到的命令

```shell
s1: 
docker build -t jenkins1:0.1 -f s5.dockerfile --force-rm .
docker run --rm -p 8999:8080 jenkins1:0.1 

s2:
docker build -t jenkins1:0.2 -f s5.dockerfile --force-rm .
docker run --rm -p 8999:8080 jenkins1:0.2 

s3:
docker build -t jenkins1:0.3 -f s5.dockerfile --force-rm .
docker run --rm -p 8999:8080 jenkins1:0.3 

s4:
docker build -t jenkins1:0.4 -f s5.dockerfile --force-rm .
docker run --rm -p 8999:8080 --env JENKINS_ADMIN_ID=admin --env JENKINS_ADMIN_PASSWORD=123456 jenkins1:0.4 

s5:
docker build -t jenkins1:0.5 -f s5.dockerfile --force-rm .
docker run --rm -p 8999:8080 --env JENKINS_ADMIN_ID=admin --env JENKINS_ADMIN_PASSWORD=123456 jenkins1:0.5

```

## 文件说明

|文件名称|作用|备注|
|----|----|----|
|plugins.txt|要安装的插件列表||
|s1.dockerfile|最基础的jenkins image||
|s2.dockerfiler|s1基础上增加了plugin的自动化安装|从plugins.txt读取plugin列表|
|s3.dockerfile|s2的基础上增加了CASC_JENKINS_CONFIG，用于配置jenkinsl||
|s4.dockerfile|s3的基础上增加了一个admin账户||
|s5.dockerfile|s4的基础上增加了权限验证||
|s3\|s4\|s5.yaml|s3,s4,s5的image所需要的jenkins yaml文件||

