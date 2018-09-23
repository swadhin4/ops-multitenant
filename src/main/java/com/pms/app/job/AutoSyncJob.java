package com.pms.app.job;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoSyncJob {

	final static Logger LOGGER = LoggerFactory.getLogger(AutoSyncJob.class);

	public void execute() {
		
		   LOGGER.info("Inside AutoSyncSchedulerServiceImpl ..  executeAutoSyncJob");
		   String operatingSystem = System.getProperty("os.name");
			File awsScript=null;
			ProcessBuilder builder=null;
			 if(operatingSystem.startsWith("Windows"))
		        {
				 	String awsBatchPath = System.getProperty("catalina.base");
				 	awsBatchPath=awsBatchPath.replaceAll("\\\\","/");
					LOGGER.info("Batch File Path : " +awsBatchPath );
					Path path = FileSystems.getDefault().getPath(awsBatchPath+"/batch/s3Sync.bat"); 
				 	String awsBatchScriptPath = path.toString();
				 	awsScript=new File(awsBatchScriptPath);
					builder= new ProcessBuilder("cmd.exe", "/c",awsScript.getAbsolutePath());
					try {
			            Process process = builder.start();
			            try {
			            	//waits for the process to complete
			            	process.waitFor();
			            	
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							LOGGER.error(e.getMessage());
						}
			           finally{
			        	   process.destroyForcibly();
			           }
			        } catch (IOException e) {
			            LOGGER.error(e.getMessage());
			        }
		        }
			 LOGGER.info("Exit AutoSyncSchedulerServiceImpl ..  executeAutoSyncJob");
	}
}