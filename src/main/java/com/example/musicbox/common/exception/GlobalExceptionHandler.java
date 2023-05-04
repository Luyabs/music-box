package com.example.musicbox.common.exception;

import com.example.musicbox.common.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;

/**
 * 全局异常处理controller
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, SignatureException.class})
    private Result argumentOrSomethingError(Exception ex) {
//        ex.printStackTrace();
        log.error("[" + ex.getClass() + "]" + ex.getMessage());
        return Result.error().message(ex.getMessage());
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
    private Result jsonParseErrorException(Exception ex) {
        String message = ex.getMessage();
        if (ex.getMessage().trim().startsWith("JSON parse error"))
            message = ex.getMessage().split(": ")[1].split(";")[0];
        else if (ex.getMessage().trim().startsWith("Required request body is missing"))
            message = "需要以JSON格式传入参数";
        else
            ex.printStackTrace();   // 未知错误
        log.error("[HttpMessageNotReadableException] " + message);
        return Result.error().message(message);
    }

    /**
     * 约束异常
     */
    @ExceptionHandler(UncategorizedSQLException.class)
    private Result uncategorizedPropertyException(Exception ex) {
        if (ex.getMessage().trim().startsWith("### Error updating database.  Cause: java.sql.SQLException: Check constraint ")) {
            String message = ex.getMessage().split(": ")[2].split("\\.")[0];
            log.error("[UncategorizedSQLException] " + " 数据库约束被打破, 请为属性更换合适的值 " + message);
            return Result.error().message(message);
        }
        ex.printStackTrace();   // 未知错误
        return Result.error().message(ex.getMessage());
    }

    /**
     * 数据库连接失败
     */
    @ExceptionHandler({SocketTimeoutException.class, CannotGetJdbcConnectionException.class})
    private Result dbConnectFailedException(Exception ex) {
        log.error("[" + ex.getClass() +  "] 数据库连接失败" + ex.getMessage());
//        ex.printStackTrace();   // 未知错误
        return Result.error().message("连接失败, 请重试");
    }

    /**
     * 主键重复
     */
    @ExceptionHandler(DuplicateKeyException.class)
    private Result duplicatePrimaryKeyException(Exception ex) {
        if (ex.getMessage().trim().startsWith("### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException")) {
            String message = ex.getMessage().split(":")[2].split("###")[0];
            log.error("[UncategorizedSQLException] " + " 主键重复 " + message);
            return Result.error().message("主键重复, 请联系后端修数据库");
        }
        ex.printStackTrace();   // 未知错误
        return Result.error().message(ex.getMessage());
    }

    /**
     * 空指针异常 应当直接处理
     */
    @ExceptionHandler(NullPointerException.class)
    private Result nullPointerException(Exception ex) {
        ex.printStackTrace();   // 未知错误, 应当直接进行处理
        return Result.error().message("空指针异常, 联系后端修复bug");
    }

    /**
     * 下载/上传文件出错
     */
    @ExceptionHandler(MultipartException.class)
    public Result uploadException(Exception ex){
        return Result.error().message("上传失败");
    }

    /**
     * 文件不存在
     */
    @ExceptionHandler(FileNotFoundException.class)
    public Result fileNotExistException(Exception ex) {
        log.error(ex.getMessage());
        return Result.error().message(ex.getMessage());
    }


}
