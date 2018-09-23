package com.pms.app.job;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AutoSyncDailyJobDetail extends QuartzJobBean {
     
	/**
	 * Logger to log statements.
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(AutoSyncDailyJobDetail.class);
    /**
     * Holds AutoSyncSchedulerService object
     */
    private AutoSyncSchedulerService autoSyncSchedulerService;
    /**
     * Holds JobExecutionContext object
     */
    private JobExecutionContext jobExecutionContext;
     
    /**
     * This method is to execute the task for the daily job.
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecContext) throws JobExecutionException {
        //JobExecutionContext is being set...
        setJobExecutionContext(jobExecContext);
         
        //First Task is being executing...
        getAutoSyncSchedulerService().executeAutoSyncJob();    
    }
 
    /**
     * getter method for autoSyncSchedulerService
     * 
     * @return AutoSyncSchedulerService
     */
    public AutoSyncSchedulerService getAutoSyncSchedulerService() {
        return autoSyncSchedulerService;
    }
 
    /**
     * setter method for autoSyncSchedulerService
     * 
     * @param autoSyncSchedulerService
     */
    public void setSchedulerService(AutoSyncSchedulerService autoSyncSchedulerService) {
        this.autoSyncSchedulerService = autoSyncSchedulerService;
    }
 
    /**
     * getter method for jobExecutionContext
     * 
     * @return JobExecutionContext
     */
    public JobExecutionContext getJobExecutionContext() {
        return jobExecutionContext;
    }
 
    /**
     * setter method for jobExecutionContext
     * 
     * @param jobExecutionContext
     */
    public void setJobExecutionContext(JobExecutionContext jobExecutionContext) {
        this.jobExecutionContext = jobExecutionContext;
    }   
}