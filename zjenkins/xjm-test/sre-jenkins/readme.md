+ 目录结构说明
    + src：标准的groovy代码
    + vars：依赖于jenkins运行环境的groovy脚本
    + resources： 静态资源文件
    + pipeline：业务相关的流水线脚本
+ jenkins的配置：
    + 某些函数是由jenkins插件提供，如readJSON由pipeline utils提供，需要安装插件
        + readJSON/writeJSON: Pipeline Utility Steps
        + withMaven: 
    + jenkins > 系统配置 > Global Pipeline Libraries，设置：
        + name=jenkinslib，这个name会在pipeline脚本中，使用 @Library('jenkinslib@dev') _ 引用
        + default version：可设置为master，也可在pipeline中，通过@Library设置
        + 选中 load implicitly，否则需要admin手动批准后才能调用。某些函数也需要被批准，如：
            + JsonOutput 
            + 
            + 运行pipeline后，在其log中会有需要批准的提示信息及链接
        + 默认会选中 allow default version to be overridden, include @Library changes in job recent changes
        + modern SCM, git, 设置其repo及credentital等
+ 000