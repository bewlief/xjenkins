package club.mydlq.jenkins.option;

import com.google.common.base.Optional;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Job(任务) 相关操作
 * <p>
 * 例如对任务的增、删、改、查等操作
 */
public class JobApi {

    private static String job_class = "hudson.model.FreeStyleProject";
    private static String folder_class = "com.cloudbees.hudson.plugins.folder.Folder";


    // Jenkins 对象
    private JenkinsServer jenkinsServer;
    // http 客户端对象
    private JenkinsHttpClient jenkinsHttpClient;

    private String JOB_NAME = "jobzx1";

    /**
     * 构造方法中调用连接 Jenkins 方法
     */
    JobApi() {
        JenkinsApi jenkinsApi = new JenkinsApi();
        // 连接 Jenkins
        jenkinsServer = JenkinsConnect.connection();
        // 设置客户端连接 Jenkins
        jenkinsHttpClient = JenkinsConnect.getClient();
    }

    /**
     * 创建 Job
     */
    public void ceateJob() {
        try {
            /**创建一个流水线任务，且设置一个简单的脚本**/
            // 创建 Pipeline 脚本
            String script = "node(){ \n" +
                    "echo 'hello world!' \n" +
                    "}";
            // xml配置文件，且将脚本加入到配置中
            String xml = "<flow-definition plugin=\"workflow-job@2.32\">\n" +
                    "<description>测试项目</description>\n" +
                    "<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.66\">\n" +
                    "<script>" + script + "</script>\n" +
                    "<sandbox>true</sandbox>\n" +
                    "</definition>\n" +
                    "</flow-definition>";
            // 创建 Job
            jenkinsServer.createJob(JOB_NAME, xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 Job
     * <p>
     * 更改之前创建的无参数Job，更改其为参数Job
     */
    public void updateJob() {
        try {
            /**
             * 更改一个流水线任务，让一个无参数的任务变成带参数任务
             */
            // 创建 Pipeline 脚本，用一个key变量
            String script = "node(){ \n" +
                    "echo \"${key}\" \n" +
                    "}";
            // xml配置文件，且将脚本加入到配置中
            String xml = "<flow-definition plugin=\"workflow-job@2.32\">\n" +
                    "<actions/>\n" +
                    "<description>测试项目</description>\n" +
                    "<keepDependencies>false</keepDependencies>\n" +
                    "<properties>\n" +
                    "<hudson.model.ParametersDefinitionProperty>\n" +
                    "<parameterDefinitions>\n" +
                    "<hudson.model.StringParameterDefinition>\n" +
                    "<name>key</name>\n" +
                    "<description>用于测试的字符变量</description>\n" +
                    "<defaultValue>hello</defaultValue>\n" +
                    "<trim>false</trim>\n" +
                    "</hudson.model.StringParameterDefinition>\n" +
                    "</parameterDefinitions>\n" +
                    "</hudson.model.ParametersDefinitionProperty>\n" +
                    "</properties>\n" +
                    "<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.66\">\n" +
                    "<script>" + script + "</script>\n" +
                    "<sandbox>true</sandbox>\n" +
                    "</definition>\n" +
                    "<disabled>false</disabled>\n" +
                    "</flow-definition>";
            // 创建 Job
            jenkinsServer.updateJob(JOB_NAME, xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Job 基本信息
     */
    public void getJob() {
        // https://dse-jenkins-ci.apps.omni.service.test/job/technology/job/arvin-xin/job/a1/
        try {
            // 获取 Job 信息
            JobWithDetails job = jenkinsServer.getJob("technology/arvin/a1");

//            JobWithDetails job = jenkinsServer.getJob(JOB_NAME);
            // 获取 Job 名称
            System.out.println(job.getName());
            // 获取 Job URL
            System.out.println(job.getUrl());
            // 获取 Job 下一个 build 编号
            System.out.println(job.getNextBuildNumber());
            // 获取 Job 显示的名称
            System.out.println(job.getDisplayName());
            // 输出 Job 描述信息
            System.out.println(job.getDescription());
            // 获取 Job 下游任务列表
            System.out.println(job.getDownstreamProjects());
            // 获取 Job 上游任务列表
            System.out.println(job.getUpstreamProjects());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Maven Job 信息
     */
    public void getMavenJob() {
        try {
            // 获取 Job 信息
            MavenJobWithDetails job = jenkinsServer.getMavenJob(JOB_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Job 列表
     */
    public void getJobList() {
        try {
            // 获取 Job 列表
            Map<String, Job> jobs = jenkinsServer.getJobs();
            for (Job job : jobs.values()) {
                System.out.println(job.getName());
                JobWithDetails jwd = jenkinsServer.getJob(job.getName());
                String url = job.getUrl();

                if (folder_class.equals(jwd.get_class())) {
                    System.out.printf("%s is a folder, url=%s%n", jwd.getName(), jwd.getUrl());

                    Optional<FolderJob> folderJobOptional = jenkinsServer.getFolderJob(job);
                    FolderJob folderJob = folderJobOptional.get();
                    Map<String, Job> jobsInFolder = folderJob.getJobs();
                    for (Job j : jobsInFolder.values()) {
                        System.out.printf("%s: %s, %s\n", j.getName(), j.getFullName(), j.getUrl());
                    }
                } else {
                    System.out.println(String.format("%s is a JOB, url=%s", jwd.getName(), jwd.getUrl()));
                }
//                FolderJob folderJob = new FolderJob(job.getName(), url);
//                if (folderJob.isFolder()) {
//                    System.out.println(String.format("{} is a folder: {}", job.getFullName(), url));
//                } else {
//                    System.out.println("Just a job");
//                }

                Optional<FolderJob> dd = jenkinsServer.getFolderJob(new Job("arvin-xin", "https://dse-jenkins-ci.apps.omni.service.test/job/technology/job/arvin-xin/"));
                FolderJob mm = dd.get();
                Map<String, Job> mk = mm.getJobs();
                for (Job k : mk.values()) {
                    System.out.printf("%s: %s, %s\n", k.getName(), k.getFullName(), k.getUrl());
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取 View 名称获取 Job 列表
     */
    public void getJobListByView() {
        try {
            // 获取 Job 列表
            Map<String, Job> jobs = jenkinsServer.getJobs("/Technology/view/OLA");
            for (Job job : jobs.values()) {
                System.out.println(job.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看 Job XML 信息
     */
    public void getJobConfig() {
        try {

            String name = "technology/arvin-xin/a1";
            Job job = jenkinsServer.getJob(name);

//            System.out.println(String.format("%s: fullname=%s, url=%s", name, job.getFullName(), job.getUrl()));
//            String xml = jenkinsServer.getJobXml(name);
//            System.out.println(xml);

            String xml = jenkinsServer.getJobXml(JOB_NAME);
            System.out.println(xml);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void testGetQueue() {
        try {
            Queue queue = jenkinsServer.getQueue();
            List<QueueItem> items = queue.getItems();
            for (QueueItem qi : items) {
                System.out.println(qi.getUrl());
                System.out.println(qi.getId());
//                Build build = jenkinsServer.getBuild(qi);
//                System.out.println(build.getUrl());
//                System.out.println(build.details().getResult());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 执行无参数 Job build
     */
    public void buildJob() {
        try {

            jenkinsServer.getJob("technology/arvin/a1").build();

//            jenkinsServer.getJob(JOB_NAME).build();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行带参数 Job build
     */
    public void buildParamJob() {
        try {
            /**
             * 例如，现有一个job，拥有一个字符参数"key"
             * 现在对这个值进行设置，然后执行一个输出这个值的脚本
             * https://blog.csdn.net/qq_33366229/article/details/104397488
             */
            // 设置参数值
            Map<String, String> param = new HashMap<>();
            param.put("name", "from java: " + LocalDateTime.now());
            // 执行 build 任务

            jenkinsServer.getJob("technology/arvin/a2").build(param);

//            jenkinsServer.getJob(JOB_NAME).build(param);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止最后构建的 Job Build
     */
    public void stopLastJobBuild() {
        try {
            // 获取最后的 build 信息
            Build build = jenkinsServer.getJob(JOB_NAME).getLastBuild();
            // 停止最后的 build
            build.Stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除 Job
     */
    public void deleteJob() {
        try {
            jenkinsServer.deleteJob(JOB_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 禁用 Job
     */
    public void disableJob() {
        try {
            jenkinsServer.disableJob(JOB_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启用 Job
     */
    public void enableJob() {
        try {

            jenkinsServer.enableJob("technology/job/arvin-xin/job/a1");

//            jenkinsServer.enableJob(JOB_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JobApi jobApi = new JobApi();
        // 创建 Job
//        jobApi.ceateJob();
        // 构建无参数的 Job
//        jobApi.buildJob();
        // 构建带参数的 Job
//        jobApi.buildParamJob();
        // 停止最后构建的 Job Build
//        jobApi.stopLastJobBuild();
        // 更新 Job
//        jobApi.updateJob();
        // 获取 Job 信息
        jobApi.getJob();
        // 获取 Maven 项目 Job
//        jobApi.getMavenJob();
        // 获取 Job 配置xml
//        jobApi.getJobConfig();
        jobApi.testGetQueue();
        // 获取全部 Job 列表
//        jobApi.getJobList();
        // 根据 view 名称获取 Job 列表
//        jobApi.getJobListByView();
        // 禁用 Job
//        jobApi.disableJob();
        // 启用 Job
//        jobApi.enableJob();
        // 删除 Job
//        jobApi.deleteJob();
    }

}
