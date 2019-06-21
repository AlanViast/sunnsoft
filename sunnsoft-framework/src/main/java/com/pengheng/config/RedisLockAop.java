package com.pengheng.config;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pengheng.core.exception.ApplicationException;

@Aspect
@Component
public class RedisLockAop {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final Log logger = LogFactory.getLog(RedisLockAop.class);
	
	@Pointcut("@annotation(com.pengheng.core.annotation.RedisLock)")
	public void lockPointCut() {}
	
	@Before("lockPointCut()")
	public void doBefore(JoinPoint joinpoint) {
		MethodSignature signature = (MethodSignature) joinpoint.getSignature();
		String className = joinpoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		logger.info("请求"+className+methodName);
	}
	
	@Around("lockPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		String key = "redis:lock:id:" + className+methodName;
		Object resultObject = null;

		Boolean res = false;
		String value = UUID.randomUUID().toString() + System.nanoTime();
		res = redisTemplate.opsForValue().setIfAbsent(key, value, 5, TimeUnit.SECONDS);
		while (res) {
			if (res) {
				try {
					res = false;
					// TODO 处理数据库操作 删除数据
					long starttime = System.currentTimeMillis();
					resultObject = joinPoint.proceed();
					long endtime = System.currentTimeMillis();
					logger.info(" COST: " + (endtime - starttime) + "ms]");
				} catch (Exception e) {
					logger.error(e);
					throw new ApplicationException("调用服务异常 sid:"+className+methodName);
				} finally {
					String redisValue = redisTemplate.opsForValue().get(key);
					if (value.equals(redisValue)) {
						redisTemplate.delete(key);
					}
				}
			} else {
				Thread.sleep(1000);
			}
		}
		return resultObject;
	}
}
