package com.example.musicbox.common.exception;

import com.example.musicbox.common.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理controller
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, SignatureException.class})
    private Result tokenWrong(Exception ex) {
        log.error("token验证失败 -- " + ex.getMessage());
        return Result.error().message("token验证失败" + ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private Result tokenExpired() {
        log.error("token已过期");
        return Result.error().message("token已过期");
    }

    /**
     * 自定义异常类 ServiceException 捕获
     */
    @ExceptionHandler({ServiceException.class})
    private Result customException(RuntimeException ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return Result.error().message(ex.getMessage());
    }

}
