package com.example.musicbox.common.interceptor;

import com.alibaba.fastjson2.JSON;
import com.example.musicbox.common.JwtUtils;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.common.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token处理拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod))    // 没有注解则跳过
            return true;
        NeedToken needToken = ((HandlerMethod) handler).getMethodAnnotation(NeedToken.class);
        if (needToken == null || !needToken.required())     // 注解不是@NeedToken则跳过token验证
            return true;

        String token = request.getHeader("token");
        try {
            long userId = Long.parseLong(JwtUtils.decodeByToken(token));
            UserInfo.set(userId);   // 写入userId到线程副本
        } catch (Exception e) {     // 拦截异常token 多半是空token或错误token
            log.error(e.getMessage());
            response.setStatus(401);
            response.getWriter().write(JSON.toJSONString(Result.error().message(e.getMessage())));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
