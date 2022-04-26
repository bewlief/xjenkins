#!/bin/bash -l
this_dir=`pwd`
echo "$this_dir ,this is pwd"
echo "$0 ,this is \$0"
dirname $0|grep "^/" >/dev/null
if [ $? -eq 0 ];then
this_dir=`dirname $0`
else
dirname $0|grep "^\." >/dev/null
retval=$?
if [ $retval -eq 0 ];then
this_dir=`dirname $0|sed "s#^.#$this_dir#"`
else
this_dir=`dirname $0|sed "s#^#$this_dir/#"`
fi
fi

#version
SVNVERSION=$1
#tempversion
TMP_VER=$2
#镜像名(job名)
IMAGE_NM=${3/\//-}
#deployment名称
DEPLOYMENTNM="msa-$IMAGE_NM"

HARBOR_IP='172.17.1.xxx'
HARBOR_USER='jenkins'
HARBOR_USER_PASSWD='********'
HARBOR_REGISTRY='172.17.1.xxx/xxxxx/'


# 判断容器是否存在
DEPLOY_NM=`kubectl get deployment | grep "$DEPLOYMENTNM" | awk '{print \$1}'`
if [ -n "$DEPLOY_NM" ]; then
# 判断镜像是否存在
IMAGE_ID=`docker images | grep "$IMAGE_NM" | awk '{print \$3}'`
if [ -n "$IMAGE_ID" ];then
#登录镜像仓库
echo "registry login"
docker login -u ${HARBOR_USER} -p ${HARBOR_USER_PASSWD} ${HARBOR_IP}
#对新镜像打标签
echo "Make Tag to $IMAGE_ID $IMAGE_NM"
docker tag "$IMAGE_NM":${TMP_VER} "$HARBOR_REGISTRY""$IMAGE_NM":${SVNVERSION}
echo "Make Tag successful"
#推送镜像
echo "Push $IMAGE_NM"
docker push "$HARBOR_REGISTRY""$IMAGE_NM":${SVNVERSION}
echo "Push $IMAGE_NM successful"
#退出镜像仓库
docker logout ${HARBOR_IP}
else
#打标签失败
echo "Make Tag Faild"
exit 1
fi
else
#打标签失败
echo "Make Tag Faild"
exit 1

fi