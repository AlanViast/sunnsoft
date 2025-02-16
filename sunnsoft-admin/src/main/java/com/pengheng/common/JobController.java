package com.pengheng.common;

import com.pengheng.quartz.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author pengheng
 * @Remark 定时任务处理类
 * @date 2019年7月23日15:37:13
 */
@RestController
@RequestMapping("/common/quartz")
public class JobController {
    @Autowired
    private JobService jobService;

    /**
     * 获取所有cron任务
     *
     * @return
     */
    @RequestMapping(value = "/getAll", method = {RequestMethod.GET, RequestMethod.POST})
    public Object startCronJob() {
        List<Map<Object, Object>> allCronJob = jobService.getAllCronJob();
        return allCronJob;
    }

    /**
     * 立即执行任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/execute", method = {RequestMethod.GET, RequestMethod.POST})
    public String executeCronJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.executeCronJob(jobName, jobGroup);
        return "create cron task success";
    }

    /**
     * 创建cron任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/cron", method = {RequestMethod.GET, RequestMethod.POST})
    public String startCronJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup,
                               @RequestParam("cron") String cron) {
        jobService.addCronJob(jobName, jobGroup, cron);
        return "create cron task success";
    }

    /**
     * 创建异步任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/async", method = {RequestMethod.GET, RequestMethod.POST})
    public String startAsyncJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.addAsyncJob(jobName, jobGroup);
        return "create async task success";
    }

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/pause", method = {RequestMethod.GET, RequestMethod.POST})
    public String pauseJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.pauseJob(jobName, jobGroup);
        return "pause job success";
    }

    /**
     * 暂停所有任务
     *
     * @return
     */
    @RequestMapping(value = "/pauseAll", method = {RequestMethod.GET, RequestMethod.POST})
    public String pauseAll() {
        jobService.pauseAllJob();
        return "pause all job success";
    }

    /**
     * 恢复任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/resume", method = {RequestMethod.GET, RequestMethod.POST})
    public String resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.resumeJob(jobName, jobGroup);
        return "resume all job success";
    }

    /**
     * 所有任务
     *
     * @return
     */
    @RequestMapping(value = "/resumeAll", method = {RequestMethod.GET, RequestMethod.POST})
    public String resumeAllJob() {
        jobService.resumeAllJob();
        return "resume all job success";
    }

    /**
     * 删除务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public String deleteJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.deleteJob(jobName, jobGroup);
        return "delete job success";
    }
}