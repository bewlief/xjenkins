# xjenkins

### xjenkins：是一套完整的 Jenkins CasC，基于本人的工作经验总结而成，包括docker image的创建、jenkins的自动化部署、与kubenetes的配合设置、pipeline的模块化等。通过 xjenkins，您可以迅速的搭建起一个完整可用的CI/CD环境。<br/>陆续完善中，敬请关注，谢谢！

### 更新历史

+ 2021/10/12 增加一个基础的jenkins image的构建过程： /jenkins-image/common/
+ --

#### 软件架构

1. jenkins image: 容器化的jenkins所需的image，包括适用于openshift环境及一般环境下的不同的image构建。<br/>

+ openshift: 基于 *** 创建
+ common image：基于官方的jenkins/jenkins:lts构建

2. jenkins configuration as code

+ --
+ --

3. library: 开发pipeline时的一些公用模块，如artifact的发布、集成checkmarx、slack的集成等

+ slack.groovy: 发布消息到slack的channel
+ --

4. pipeline： pipeline job的集中管理

+ --

5. --

#### 使用说明

1. xxxx
2. xxxx
3. xxxx
