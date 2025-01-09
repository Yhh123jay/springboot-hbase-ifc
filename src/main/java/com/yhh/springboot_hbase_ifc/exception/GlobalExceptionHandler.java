package com.yhh.springboot_hbase_ifc.exception;

import com.yhh.springboot_hbase_ifc.model.vo.Result;
import com.yhh.springboot_hbase_ifc.model.vo.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 * 使用@ControllerAdvice注解的类可以拦截所有Controller的异常
 * 使用@ExceptionHandler注解的方法可以处理异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理,，可以捕获所有控制器的异常
    // @ExceptionHandler注解用来指定处理的异常类型
    // 如果这个异常类型发生了，就会调用这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.build(null, ResultCodeEnum.FAIL);
    }

    // 自定义异常处理
    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result ierror(GuiguException e){
        return Result.build(null, e.getResultCodeEnum());
    }
}
