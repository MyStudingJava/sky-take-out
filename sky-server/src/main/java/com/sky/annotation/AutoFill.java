package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识需要自动填充的字段
 */
@Target(ElementType.METHOD) // 指定注解加在方法上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时存在
public @interface AutoFill {
    // 指定属性,当前数据库操作的类型
    OperationType value(); // 数据库操作类型:UPDATE,INSERT
}
