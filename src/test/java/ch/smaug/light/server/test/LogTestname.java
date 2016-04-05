package ch.smaug.light.server.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogTestname {
	private final static Logger LOG = LoggerFactory.getLogger(LogTestname.class);

	@Pointcut("@annotation(org.junit.Test)")
	public void annotationPointCutDefinition() {
	}

	@Pointcut("execution(* *(..))")
	public void atExecution() {
	}

	@Around("annotationPointCutDefinition() && atExecution()")
	public Object printTestName(final ProceedingJoinPoint joinPoint) throws Throwable {
		final long start = System.currentTimeMillis();
		try {
			LOG.debug("*** Starting {}", joinPoint.getSignature().toShortString());
			return joinPoint.proceed();
		} finally {
			final long end = System.currentTimeMillis();
			LOG.debug("*** Done {} in {} ms", joinPoint.getSignature().toShortString(), end - start);
		}
	}
}
