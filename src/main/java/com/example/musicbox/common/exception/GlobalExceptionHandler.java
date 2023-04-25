package com.example.musicbox.common.exception;

import com.example.musicbox.common.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.UncategorizedSQLException;
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
        log.error("[" + ex.getClass() + "] token验证失败 -- " + ex.getMessage());
        return Result.error().message("token验证失败" + ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private Result tokenExpired() {
        log.error("[ExpiredJwtException] " + "token已过期");
        return Result.error().message("token已过期");
    }

    /**
     * 自定义异常类 ServiceException 捕获
     */
    @ExceptionHandler({ServiceException.class})
    private Result customException(Exception ex) {
//        ex.printStackTrace();
        log.error("[ServiceException] " + ex.getMessage());
        return Result.error().message(ex.getMessage());
    }

    /**
     * 字符串数据过长
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    private Result dataOutOfRangeException(Exception ex) {
        if (ex.getMessage().trim().startsWith("### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column")) {
            String message = ex.getMessage().split("'")[1] + "属性过长";
            log.error("[DataIntegrityViolationException] " + message);
            return Result.error().message(message);
        }
        ex.printStackTrace();   // 未知错误
        return Result.error().message(ex.getMessage());
    }

    /**
     * 属性不对
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private Result dataIncorrectPropertyException(Exception ex) {
        if (ex.getMessage().trim().startsWith("JSON parse error")) {
            String message = ex.getMessage().split(": ")[1].split(";")[0];
            log.error("[HttpMessageNotReadableException] " + message);
            return Result.error().message(message);
        }
        ex.printStackTrace();   // 未知错误
        return Result.error().message(ex.getMessage());
    }

    /**
     * 约束异常
     */
    @ExceptionHandler(UncategorizedSQLException.class)
    private Result uncategorizedPropertyException(Exception ex) {
        if (ex.getMessage().trim().startsWith("### Error updating database.  Cause: java.sql.SQLException: Check constraint ")) {
            String message = ex.getMessage().split(": ")[2].split("\\.")[0];
            log.error("[UncategorizedSQLException] " + " 出现这一条意味着数据库约束被打破, 请更换合适的值 " + message);
            return Result.error().message(message);
        }
        ex.printStackTrace();   // 未知错误
        return Result.error().message(ex.getMessage());
    }
}
