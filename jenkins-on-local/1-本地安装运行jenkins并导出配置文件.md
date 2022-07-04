# 如何在本机快速运行起jenkins

有些时候为了快速验证某个修改，使用本机的jenkins server是最方便的了。本文档即描述了如何在本地运行起来jenkins。
## 基本步骤
1. 需要安装配置jdk

请参考网络文档。建议你使用 xshell：https://gitee.com/bewlief/xshell，https://github.com/bewlief/xshell，快速搭建你的开发环境。

2. 下载jenkins的package
+ 官网下载： https://www.jenkins.io/download/
+ 我一般是用generic war+jd去运行：
+ windows下还可以用： https://www.jenkins.io/download/thank-you-downloading-windows-installer-stable/，不过还是需要jdk。会将jenkins安装为服务。
+ 
3. 运行
+ 命令行下，运行 java -jar <path-of-your-jenkins-war> 即可。
  + 会在你的用户目录下，创建 .jenkins 文件夹，该文件夹是jenkins的配置、数据文件所在目录，最好维护好，不要贸然删除。当然，如果你搞坏了，直接删除该目录，再重新启动，就又是一个干净的jenkins了。
  
  注意，此时是绝对干净了，你之前的配置也都会丢失哦。
  + 运行后，会提示一个password，如下面所示：
```bash

Jenkins initial setup is required. An admin user has been created and a password generated.
Please use the following password to proceed to installation:

379819655a69474ea410fca8a9730dc6

This may also be found at: C:\Users\xjming\.jenkins\secrets\initialAdminPassword
```
这个密码，是admin账户的初始化密码，第一次登陆时需要。
  + 从启动界面还可以看到如下内容：
```bash
2022-06-21 00:40:24.144+0000 [id=1]     INFO    hudson.WebAppMain#contextInitialized: Jenkins home directory: C:\Users\xjming\.jenkins found at: $user.home/.jenkins
2022-06-21 00:40:24.509+0000 [id=1]     INFO    o.e.j.s.handler.ContextHandler#doStart: Started w.@2b9f74d0{Jenkins v2.332.3,/,file:///C:/Users/xjming/.jenkins/war/,AVAILABLE}{C:\Users\xjming\.jenkins\war}
2022-06-21 00:40:24.546+0000 [id=1]     INFO    o.e.j.server.AbstractConnector#doStart: Started ServerConnector@3382f8ae{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
```
提示你的数据文件所在目录是 C:\Users\xjming\.jenkins\，及web服务所用的端口是 8080
  + 登录 http://localhost:8080，输入admin密码，选择并安装plugin后(可暂时忽略)，进入界面。
可以开始愉快的玩耍了！

## 建议安装的plugin
+ [必需] Configuration as Code

Jenkins CasC的实现，完全依赖于此。

+ --
+ --

## 本机的jcasc
1. 检查下Configuration as Code是否已安装

Dashboard > manage jenkins > manage plugins > 已安装，搜索 configuration as code
2. jenkins做好配置后，可以导出设置

Dashboard > manage jenkins > Configuration as Code > 下载配置

会保存为一个 jenkins.yaml 文件，这个就是我们以后jenkins casc的基础文件了。下面是我本地初始化jenkins后导出的文件内容：
```yaml
jenkins:
  agentProtocols:
  - "JNLP4-connect"
  - "Ping"
  authorizationStrategy:
    loggedInUsersCanDoAnything:
      allowAnonymousRead: false
  crumbIssuer:
    standard:
      excludeClientIPFromCrumb: false
  disableRememberMe: false
  labelAtoms:
  - name: "built-in"
  markupFormatter: "plainText"
  mode: NORMAL
  myViewsTabBar: "standard"
  noUsageStatistics: true
  nodeProperties:
  - envVars:
      env:
      - key: "MAVEN_OPTS"
        value: "-Xms512m -Xmx2G -Dfile.encoding=UTF-8"
  - envVars:
      env:
      - key: "MAVEN_OPTS"
        value: "-Xms512m -Xmx2G -Dfile.encoding=UTF-8"
  - envVars:
      env:
      - key: "MAVEN_OPTS"
        value: "-Xms512m -Xmx2G -Dfile.encoding=UTF-8"
  - envVars:
      env:
      - key: "MAVEN_OPTS"
        value: "-Xms512m -Xmx2G -Dfile.encoding=UTF-8"
  numExecutors: 2
  primaryView:
    all:
      name: "all"
  projectNamingStrategy: "standard"
  quietPeriod: 5
  remotingSecurity:
    enabled: true
  scmCheckoutRetryCount: 0
  securityRealm:
    local:
      allowsSignup: false
      enableCaptcha: false
      users:
      - id: "xjming"
        name: "jianmingxin"
        properties:
        - "userCommunityProperty"
        - "myView"
        - preferredProvider:
            providerId: "default"
        - "timezone"
        - mailer:
            emailAddress: "71935665@qq.com"
        - "apiToken"
  slaveAgentPort: -1
  updateCenter:
    sites:
    - id: "default"
      url: "https://updates.jenkins.io/update-center.json"
  views:
  - all:
      name: "all"
  viewsTabBar: "standard"
globalCredentialsConfiguration:
  configuration:
    providerFilter: "none"
    typeFilter: "none"
security:
  apiToken:
    creationOfLegacyTokenEnabled: false
    tokenGenerationOnCreationEnabled: false
    usageStatisticsEnabled: true
  envInject:
    enableLoadingFromMaster: false
    enablePermissions: false
    hideInjectedVars: false
  sSHD:
    port: -1
unclassified:
  buildDiscarders:
    configuredBuildDiscarders:
    - "jobBuildDiscarder"
  buildStepOperation:
    enabled: false
  email-ext:
    adminRequiredForTemplateTesting: false
    allowUnregisteredEnabled: false
    charset: "UTF-8"
    debugMode: false
    defaultBody: |-
      $PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:

      Check console output at $BUILD_URL to view the results.
    defaultContentType: "text/plain"
    defaultSubject: "$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!"
    defaultTriggerIds:
    - "hudson.plugins.emailext.plugins.trigger.FailureTrigger"
    maxAttachmentSize: -1
    maxAttachmentSizeMb: -1
    precedenceBulk: false
    watchingEnabled: false
  fingerprints:
    fingerprintCleanupDisabled: false
    storage: "file"
  gitHubConfiguration:
    apiRateLimitChecker: ThrottleForNormalize
  gitHubPluginConfig:
    hookUrl: "http://localhost:8080/github-webhook/"
  gitSCM:
    addGitTagAction: false
    allowSecondFetch: false
    createAccountBasedOnEmail: false
    disableGitToolChooser: false
    hideCredentials: false
    showEntireCommitSummaryInChanges: false
    useExistingAccountWithSameEmail: false
  globalTimeOutConfiguration:
    operations:
    - "abortOperation"
  junitTestResultStorage:
    storage: "file"
  location:
    adminAddress: "address not configured yet <nobody@nowhere>"
    url: "http://localhost:8080/"
  mailer:
    charset: "UTF-8"
    useSsl: false
    useTls: false
  mavenModuleSet:
    localRepository: "default"
  pollSCM:
    pollingThreadCount: 10
  sshPublisher:
    commonConfig:
      disableAllExec: false
      encryptedPassphrase: "{AQAAABAAAAAQyjW5/rmEqAoz+U0AEW5NDtvlkA7P/YHwoufxLLCRHSw=}"
  timestamper:
    allPipelines: false
    elapsedTimeFormat: "'<b>'HH:mm:ss.S'</b> '"
    systemTimeFormat: "'<b>'HH:mm:ss'</b> '"
tool:
  git:
    installations:
    - home: "git.exe"
      name: "Default"
  mavenGlobalConfig:
    globalSettingsProvider: "standard"
    settingsProvider: "standard"

```
可见其内容复杂，完全手工去编写是非常困难的。因此，我的一般做法就是，在本地jenkins做好配置好，导出文件，再做适当修改。 


### OK，到这里为止，你的jenkins已经开始运行了，下一步让我们来创建个简单的job耍一下吧。
