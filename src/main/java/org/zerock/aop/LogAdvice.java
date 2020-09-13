package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect
@Log4j
@Component
public class LogAdvice {
	@Before("execution(public * org.zerock.service.*.*(..))")	// public 추가 시 실행
	public void logBefore() {
		log.info("====== Log Advice =======");
	}
	
	@Before("execution(* org.zerock.service.SampleService*.doAdd(String, String)) && args(str1,str2)")	// args 를 이용한 AOP
	public void logBeforeWithParam(String str1, String str2) {
		log.info("str1 : " + str1);
		log.info("str2 : " + str2); 
	}
	
	// 지정된 대상이 예외를 발생한 후에 동작(문제 발견에 유용)
	@AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing="exception")
	public void logException(Exception exception) {
		log.info("Exception...........!!");
		log.info("exception : " + exception);
	}
	// @Around 직접 대상 메서드를 실행할 수 있는 권한을 가지고 있음, 메서드의 실행 전과 실행 후에 처리가 가능
	@Around("execution(* org.zerock.service.SampleService*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		long start = System.currentTimeMillis();
		
		log.info("Target : " + pjp.getTarget());
		log.info("Param : " + Arrays.toString(pjp.getArgs()));
		
		// invoke method
		Object result = null;
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		log.info("TIME : " + (end - start));
		
		return result;
	}
}
