package com.cuit.alias.handler;


import com.cuit.alias.common.constant.ResultEnum;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.CommonResult;
import com.cuit.alias.common.result.ResultUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author lisihan
 * @version 1.0
 **/
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public CommonResult<String> error(Exception e) {
        e.printStackTrace();
        return ResultUtils.fail();
    }

    @ExceptionHandler(AppException.class)
    public CommonResult<String> error(AppException e) {
        e.printStackTrace();
        return ResultUtils.fail(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<String> error(ConstraintViolationException e) {
        e.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder res = new StringBuilder("参数异常: ");
        constraintViolations.forEach(c -> res.append(c.getMessage()).append(" "));
        return ResultUtils.fail(res.toString().trim());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public CommonResult<String> argumentError(Exception e) {
        e.printStackTrace();
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        }
        StringBuilder msg = new StringBuilder();
        assert bindingResult != null;
        bindingResult.getFieldErrors().forEach((fieldError) ->
                msg.append(fieldError.getDefaultMessage()).append(" ")
        );
        return ResultUtils.fail(msg.toString().trim());
    }

    @ExceptionHandler(NullPointerException.class)
    public CommonResult<String> error(NullPointerException e) {
        e.printStackTrace();
        return ResultUtils.fail("发生了空指针异常,请联系管理员解决");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult<String> error(AccessDeniedException e) {
        e.printStackTrace();
        return ResultUtils.fail(ResultEnum.PERMISSION_DENIED);
    }
}
