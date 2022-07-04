# 此目录下存放应用测试的配置文件

# 文件名称格式
+ {app-name}-{pipeline-type}.json
+ 如 eureka-full.json，表示应用 eureka，使用 full 类型的pipeline。jenkins中的job名称，要和此文件名称完全匹配。

# 文件内容
+ 范例<br/>
```
{
  "repoUrl": "http://www.git.com/gateway",
  "branch": "xjm",
  "stages": "unit-test,
            static-check,
            deploy-test,
            api-test,
            ui-test,
            perf-test, 
            send-report,
            deploy-prod"
}
```
+ 说明
    + repoUrl: 该应用使用的git repo url
    + branch: 使用的分支
    + stages：需要包含的stage内容，多选项，按预定顺序，以“,”分割：
        + unit-test： 单元测试
        + static-check： 静态测试
        + deploy-test：部署到测试环境
        + api-test：api测试
        + ui-test：ui的自动化测试
        + perf-test： 性能测试
        + send-report： 发送report给指定人员
        + deploy-pro： 部署到生产环境中