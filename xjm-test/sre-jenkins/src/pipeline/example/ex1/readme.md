+ https://www.jianshu.com/p/3d5a065ffec9
+ php+js:<br/>
与java不同的是，我们php+js的服务使用了3个容器，php+nginx+code的方式进行部署，每次只需要更新服务中的code容器即可；另外，为了防止php和js的构建污染jenkins服务，我们利用了官方的node镜像和一个自定义已安装好php构建环境的composer镜像，并使用docker.image('').inside{}代码块将构建过程放置到了docker容器当中。
+ 邮件模板
    + 标题: 构建通知:${BUILD_STATUS} - ${PROJECT_NAME} -  # ${BUILD_NUMBER}!
    + 内容： 
    ```
    <!DOCTYPE html>
    <html>
    <head>
    <meta charset="UTF-8">
    <title>${ENV, var="JOB_NAME"}-第${BUILD_NUMBER}次构建日志</title>
    </head>
    
    <body leftmargin="8" marginwidth="0" topmargin="8" marginheight="4"
        offset="0">
        <table width="95%" cellpadding="0" cellspacing="0"
            style="font-size: 11pt; font-family: Tahoma, Arial, Helvetica, sans-serif">
            <tr>
                <td>(本邮件是程序自动下发的，请勿回复！)</td>
            </tr>
            <tr>
                <td><h2>
                        <font color="#0000FF">构建结果 - ${BUILD_STATUS}</font>
                    </h2></td>
            </tr>
            <tr>
                <td><br />
                <b><font color="#0B610B">构建信息</font></b>
                <hr size="2" width="100%" align="center" /></td>
            </tr>
            <tr>
                <td>
                    <ul>
                        <li>项目名称&nbsp;：&nbsp;${PROJECT_NAME}</li>
                        <li>构建编号&nbsp;：&nbsp;第${BUILD_NUMBER}次构建</li>
                        <li>触发原因：&nbsp;${CAUSE}</li>
                        <li>构建日志：&nbsp;<a href="${BUILD_URL}console">${BUILD_URL}console</a></li>
                        <li>构建&nbsp;&nbsp;Url&nbsp;：&nbsp;<a href="${BUILD_URL}">${BUILD_URL}</a></li>
                        <li>项目&nbsp;&nbsp;Url&nbsp;：&nbsp;<a href="${PROJECT_URL}">${PROJECT_URL}</a></li>
                    </ul>
                </td>
            </tr>
            <tr>
                <td><b><font color="#0B610B">Changes Since Last
                            Successful Build:</font></b>
                <hr size="2" width="100%" align="center" /></td>
            </tr>
            <tr>
                <td>
                    <ul>
                        <li>历史变更记录 : <a href="${PROJECT_URL}changes">${PROJECT_URL}changes</a></li>
                    </ul> ${CHANGES_SINCE_LAST_SUCCESS,reverse=true, format="Changes for Build #%n:<br />%c<br />",showPaths=true,changesFormat="<pre>[%a]<br />%m</pre>",pathFormat="&nbsp;&nbsp;&nbsp;&nbsp;%p"}
                 <br>
                </td>
            </tr>
            <tr>
                <td><b><font color="#0B610B">Failed Test Results</font></b>
                <hr size="2" width="100%" align="center" /></td>
            </tr>
            <tr>
                <td><pre
                        style="font-size: 11pt; font-family: Tahoma, Arial, Helvetica, sans-serif">$FAILED_TESTS</pre>
                    <br></td>
            </tr>
            <tr>
                <td><b><font color="#0B610B">构建日志 (最后 100行):</font></b>
                <hr size="2" width="100%" align="center" /></td>
            </tr>
            <tr>
                <td><textarea id="test" cols="80" rows="30" readonly="readonly"
                        style="font-family: Courier New">${BUILD_LOG, maxLines=100}</textarea>
                </td>            
            </tr>
        </table>
    </body>
    </html>
    ```