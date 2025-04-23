package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

        // 1. 获取到当前被拦截的方法上的数据库操作类型
        // Signature signature = joinPoint.getSignature();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 方法签名转型
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获得方法上的注解对象
        OperationType operationType = autoFill.value(); // 获取到注解上的数据库操作类型


        // 2. 获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 3. 准备赋值数据
        LocalDateTime now = LocalDateTime.now(); // 时间
        Long currentId = BaseContext.getCurrentId(); // 人

        // 4. 根据当前不同的操作类型,为对应的实体对象通过反射来赋值
        if (operationType == OperationType.INSERT) {
            // 为4个公共字段赋值
            try {
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(entity, currentId);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType == OperationType.UPDATE) {
            // 为两个公共字段赋值
            try {
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
