#!/bin/sh
#
# Copyright (c) 2012-2015 Andrea Selva
#
#需要启动的Java主程序（main方法类）
APP_MAINCLASS=com.turingcat.ServerApp

psid=0

#启动脚本的参数
param1="$1"

checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}

start(){
    checkpid
    if [ $psid -ne 0 ]; then
        echo "================================"
        echo "warn: $APP_MAINCLASS 服务已经启动! (pid=$psid)"
        echo "================================"
    else
        echo -n "正在启动服务 $APP_MAINCLASS ..."
        nohup $JAVA -server $JAVA_OPTS $JAVA_OPTS_SCRIPT -Dname=lanprotocolServer -Dlogging.config="file:$LOG_FILE" -Dlan.path="$LAN_PATH" -cp "$LAN_HOME/lib/*" $APP_MAINCLASS >/dev/null 2>&1 &
    fi
}

stop(){
   checkpid
   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_MAINCLASS ...(pid=$psid) "
      kill -9 $psid
      if [ $? -eq 0 ]; then     # $?:代表马上查看返回值
         echo "[OK]kill命令执行成功"
      else
         echo "[Failed]kill命令执行失败"
      fi
      checkpid
      if [ $psid -ne 0 ]; then
        echo "================================"
        echo "warn: $APP_MAINCLASS 服务未停止成功"
        echo "================================"
      else
        echo "================================="
        echo "success:$APP_MAINCLASS 服务停止成功"
        echo "================================="
      fi
   else
      echo "================================"
      echo "warn: $APP_MAINCLASS is not running"
      echo "================================"
   fi

}

restart(){
    stop
    start
}

cd "$(dirname "$0")"

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set LAN_HOME if not already set
[ -f "$LAN_HOME"/bin/lanprotocol.sh ] || LAN_HOME=`cd "$PRGDIR/.." ; pwd`
export LAN_HOME

# Set JavaHome if it exists
if [ -f "${JAVA_HOME}/bin/java" ]; then 
   JAVA=${JAVA_HOME}/bin/java
else
   JAVA=java
fi
export JAVA

LOG_FILE=$LAN_HOME/config/logback.xml
LAN_PATH=$LAN_HOME/
#LOG_CONSOLE_LEVEL=info
#LOG_FILE_LEVEL=fine
JAVA_OPTS_SCRIPT="-Xms200m -Xmx200m -Xss256k -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/logs/javaHeapDump.hprof -Djava.awt.headless=true"

#判断字符串是否相等
if [ "$param1" = "start" ];then
    start
elif [ "$param1" = "stop" ];then
    stop
elif [ "$param1" = "restart" ];then
    restart
else
    echo "输入命令错误：$param1,可输入:(start|stop|restart)"
fi



