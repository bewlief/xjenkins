# 配置jenkins.war的自启动
+ chmod 755 jenkins.sh
+ ln -s /root/xjm/jenkins/jenkins.sh /usr/local/bin/jenkins.sh
+ cp jenkins.service /etc/systemd/system/jenkins.service
+ chmod 664 /etc/systemd/system/jenkins.service
+ systemctl daemon-reload
+ systemctl start jenkins

+ 注意文件从windows复制到linux上时的^M即换行符问题！上传到linux后：
    + dos2unix jenkins.sh 或者 
    + sed -e 's/^M/\n/g' jenkins.sh