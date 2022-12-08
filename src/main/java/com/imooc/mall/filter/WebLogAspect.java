package com.imooc.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 描述： 过滤器类，作用是：过滤请求与响应，打印请求与响应的信息；
 */
@Aspect
@Component
public class WebLogAspect {
    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);


    @Pointcut("execution(public * com.imooc.mall.controller.*.*(..))")
    public void webLog() {

    }

    /**
     * 处理请求的信息
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("URL :" + request.getRequestURL().toString());//请求的url信息
        log.info("HTTP_METHOD :" + request.getMethod());//请求的类型
        log.info("IP :" + request.getRemoteAddr());//请求的ip地址
        //目标类/目标方法的：类和方法名
        log.info("CLASS_METHOD" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //请求的参数
        log.info("ARGS :" + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 处理响应的信息
     * @param res
     * @throws JsonProcessingException
     */
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //处理完请求，返回内容
        log.info("RESPONSE :" + new ObjectMapper().writeValueAsString(res));

    }
}