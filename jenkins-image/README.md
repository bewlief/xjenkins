# Jenkins docker image build

jenkins的image，分为两种，一是适用于openshift，采用openshift的jenkins image创建；二是一般环境下的image，使用官方的jenkins/jenkins:lts构建而成。<br/>
自动逸image的目的，是对jenkins进行定制化处理：<br/>

+ 自动配置jenkins的运行时环境
+ 安装指定的插件
+ 导入jenkins.yaml，从而自动化配置所需的各种参数，如credential、cloud等
+ 自动创建预定义的jenkins job

