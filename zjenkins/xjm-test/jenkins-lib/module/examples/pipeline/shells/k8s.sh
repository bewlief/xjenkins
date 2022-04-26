#!/bin/bash -l

#k8s对外端口（根据分支名来区分）
TESTPORT=$1
#version
SVNVERSION=$2
#镜像名
IMAGE_NM=$3
#deployment名称
DEPLOYMENTNM="msa-$IMAGE_NM"
#k8s服务端口、容器端口测试
SERVICEPORT='8890'

#复制k8s脚本模板
cp datamgt.yaml $IMAGE_NM.yaml

#修改各项参数
echo '指定节点'
sed -i "s#node-x#$MY_NODE_NAME#g" $IMAGE_NM.yaml
echo '指定服务名'
sed -i "s#k8sservicenm#$IMAGE_NM#g" $IMAGE_NM.yaml
echo '指定deployment名'
sed -i "s#deploymentnm#$DEPLOYMENTNM#g" $IMAGE_NM.yaml
echo '指定容器使用的镜像版本'
sed -i "s#imagetemp#$SVNVERSION#g" $IMAGE_NM.yaml
echo '指定k8s服务端口、容器端口'
sed -i "s#service-port#$SERVICEPORT#g" $IMAGE_NM.yaml
#根据分支指定k8s对外端口
echo "指定容器端口"
sed -i "s#k8s-port#$TESTPORT#g" $IMAGE_NM.yaml

# 删除旧deployment
DEPLOY_NM=`kubectl get deployment | grep "$DEPLOYMENTNM" | awk '{print \$1}'`
if [ -n "$DEPLOY_NM" ];then
kubectl delete -f $IMAGE_NM.yaml
echo "$DEPLOY_NM delete"
fi

# 执行k8s脚本
echo "执行k8s脚本"
kubectl create -f $IMAGE_NM.yaml