#!/usr/bin/env bash
### 主要目的用于开机启动服务,不然 启动jenkins.war包没有java -jar的权限
### jenkins.sh start | stop
### 注意第一行不要有空行，否则systemctl start时会报 exec format error

RETVAL=0
EXEC="/root/xjm/jenkins.sh"
JENKINS_WAR="/root/xjm/jenkins.war"
OPTIONS="--httpPort=9000"
PROG="Jenkins.war"
LOCKFILE="/var/lock/subsys/jenkins.pid"
JAVA_HOME=/root/xjm/jdk8

# Source function library.
if [ -f /etc/rc.d/init.d/functions ]; then
  . /etc/rc.d/init.d/functions
else
  echo "/etc/rc.d/init.d/functions does not exist"
  exit 0
fi

pid=`ps -ef | grep jenkins.war | grep -v 'grep'| awk '{print $2}'| wc -l`
#
#if [ "$1" = "start" ];then
#  if [ $pid -gt 0 ];then
#    echo 'jenkins is running...'
#  else
#    ### java启动服务 配置java安装根路径,和启动war包存的根路径
#    nohup $JAVA_HOME/bin/java -jar /root/xjm/jenkins.war --httpPort=9000  2>&1 &
#  fi
#elif [ "$1" = "stop" ];then
#    exec ps -ef | grep jenkins | grep -v grep | awk '{print $2}'| xargs kill -9
#    echo 'jenkins is stop..'
#else
#    echo "Please input like this:"./jenkins.sh start" or "./jenkins stop""
#fi

# Log that something succeeded
success() {
    [ "$BOOTUP" != "verbose" -a -z "${LSB:-}" ] && echo_success
    return 0
}

# Log that something failed
failure() {
    local rc=$?
    [ "$BOOTUP" != "verbose" -a -z "${LSB:-}" ] && echo_failure
    [ -x /bin/plymouth ] && /bin/plymouth --details
    return $rc
}


start() {
  if [ -f $LOCKFILE ]; then
    echo "$PROG is already running!"
  else
    echo -n "Starting $PROG: "
    nohup $JAVA_HOME/bin/java -jar $JENKINS_WAR $OPTIONS 2>&1 &
    RETVAL=$?
    [ $RETVAL -eq 0 ] && touch $LOCKFILE && success || failure
    echo
    return $RETVAL
  fi
}

stop() {
  echo -n "Stopping $PROG: "
  # killproc $EXEC，如killproc "/root/xjm/java"这样才能生效。但必然会杀掉其他java进程！
  exec ps -ef | grep "$JAVA_HOME/bin/java -jar $JENKINS_WAR" | grep -v grep | awk '{print $2}'| xargs kill -9
  RETVAL=$?
  [ $RETVAL -eq 0 ] && rm -r $LOCKFILE && success || failure
  echo
}

restart() {
  stop
  sleep 1
  start
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  status)
    status $PROG
    ;;
  restart)
    restart
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
esac
exit $RETVAL