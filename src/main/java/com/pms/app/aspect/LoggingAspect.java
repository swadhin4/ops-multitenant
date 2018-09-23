package com.pms.app.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {

	private static final Logger LOGGER=LoggerFactory.getLogger(LoggingAspect.class);

	/*	@Around("execution(* com.web.service.impl.EmployeeServiceImpl.saveEmployee(..))")
	public void saveEmployeelogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Before Calling Save Employee : ");
		LOGGER.info("Before Method Called : " + joinPoint.getSignature().getName());
		LOGGER.info("Method arguments : " + Arrays.toString(joinPoint.getArgs()));
		LOGGER.info("Around before is running!");
		joinPoint.proceed(); //continue on the intercepted method
		LOGGER.info("Around after is running!");
		LOGGER.info("******Save Employee Method Called******");
	}

	@Around("execution(* com.web.service.impl.VendorServiceImpl.saveVendor(..))")
	public void saveVendorlogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Before Calling Save Vendor : ");
		LOGGER.info("Before Method Called : " + joinPoint.getSignature().getName());
		LOGGER.info("Method arguments : " + Arrays.toString(joinPoint.getArgs()));
		LOGGER.info("Around before is running!");
		joinPoint.proceed(); //continue on the intercepted method
		LOGGER.info("Around after is running!");
		LOGGER.info("******Save Vendor Method Called******");
	}

	@Around("execution(* com.web.service.impl.GradeServiceImpl.saveGrade(..))")
	public void saveGradelogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Before Calling Save Grade : ");
		LOGGER.info("Before Method Called : " + joinPoint.getSignature().getName());
		LOGGER.info("Method arguments : " + Arrays.toString(joinPoint.getArgs()));
		LOGGER.info("Around before is running!");
		joinPoint.proceed(); //continue on the intercepted method
		LOGGER.info("Around after is running!");
		LOGGER.info("******Save Grade Method Called******");
	}*/
}
