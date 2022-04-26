#!/bin/bash -l

#deployment名
DEPLOYMENTNM=${1/\//-}
#镜像标签
TEMPTAG=$2
# 删除正在运行的deployment
DEPLOY_NM=`kubectl get deployment | grep "$DEPLOYMENTNM" | awk '{print \$1}'`

if [ -n "$DEPLOY_NM" ] && [ -f "docker_shell/$DEPLOYMENTNM.yaml" ];then
kubectl delete -f docker_shell/$DEPLOYMENTNM.yaml
echo "$DEPLOY_NM 已删除！！！"
docker rmi $DEPLOYMENTNM:$TEMPTAG
echo "$DEPLOYMENTNM:$TEMPTAG 镜像已删除！！！"
else
echo "$DEPLOY_NM 未删除！！！"
fi