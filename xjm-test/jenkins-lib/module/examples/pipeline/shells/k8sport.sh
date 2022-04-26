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

#testport
TESTPORT=$1
#version
SVNVERSION=$2
#镜像名(job名)
IMAGE_NM=${3/\//-}

HARBOR_IP='172.17.1.XXX'
HARBOR_USER='jenkins'
HARBOR_USER_PASSWD='******'


# 删除旧镜像
IMAGE_ID=`docker images | grep "$IMAGE_NM" | awk '{print \$3}'`
if [ -n "$IMAGE_ID" ];then
docker rmi $IMAGE_ID
echo "$IMAGE_ID delete"
fi

#登录镜像仓库
docker login -u ${HARBOR_USER} -p ${HARBOR_USER_PASSWD} ${HARBOR_IP}

# 构建新镜像
cd $this_dir
docker build -t "$IMAGE_NM":${SVNVERSION} .

# 判断构建是否成功
IMAGE_ID=`docker images | grep "$IMAGE_NM" | awk '{print \$3}'`
if [ -n "$IMAGE_ID" ];then
echo "$IMAGE_ID $IMAGE_NM 镜像制作成功"
#退出仓库
docker logout ${HARBOR_IP}

else
#如果制作镜像失败了，退出
echo "$IMAGE_NM $IMAGE_NM 镜像制作失败"
#退出仓库
docker logout ${HARBOR_IP}
exit 1

fi

# 配置k8s脚本
echo "配置k8s脚本"
./k8sport.sh $TESTPORT $SVNVERSION $IMAGE_NM