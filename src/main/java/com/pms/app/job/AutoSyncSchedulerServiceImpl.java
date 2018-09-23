package com.pms.app.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoSyncSchedulerServiceImpl implements AutoSyncSchedulerService {
	
	final static Logger LOGGER = LoggerFactory.getLogger(AutoSyncSchedulerServiceImpl.class);
	
	private AutoSyncJob autoSyncJob;
	
	@Override
	public void executeAutoSyncJob() {
		autoSyncJob.execute();
	}
	
	public AutoSyncJob getAutoSyncJob() {
		return autoSyncJob;
	}

	public void setAutoSyncJob(AutoSyncJob autoSyncJob) {
		this.autoSyncJob = autoSyncJob;
	}


}
