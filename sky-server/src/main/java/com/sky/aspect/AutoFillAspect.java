package com.sky.aspect;

import com.sky.annotation.AutoFill;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 自定义切面,实现自动填充功能
 */
@Aspect // 表示当前类是一个切面类
@Component // 表示当前类是一个组件,会被Spring容器扫描到并管理
@Slf4j // 表示当前类使用log4j日志记录
public class AutoFillAspect {
    /**
     * 切入点
     * 用于对哪些类的哪些方法进行拦截
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){ }

    /**
     * 需要对拦截后的公共字段进行赋值处理 ---> 在通知里进行处理
     * 其中拦截分为:前置通知,后置通知,环绕通知,异常通知等等
     *
     * 这里需要前置通知,前置通知在方法执行前执行,对公共字段进行赋值处理
     */
    @Before("autoFillPointCut()") // 指定切入点,就是方法名称
    public void autoFill(JoinPoint joinPoint){ // JoinPoint表示当前切面方法
        log.info("开始进行公共字段自动填充...");
    }
}
