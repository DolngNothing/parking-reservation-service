package com.oocl.parkingreservationservice.utils.interceptor;

import com.oocl.parkingreservationservice.exception.NotLoginException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XUAL7
 */
public class LoginInterceptor implements HandlerInterceptor {

    public static final String USER_PHONE = "userPhone";
    public static final String USER_NAME = "userName";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        Object userName = request.getSession().getAttribute(USER_NAME);
        Object userPhone = request.getSession().getAttribute(USER_PHONE);
        if (userName == null || userPhone == null) {
            throw new NotLoginException();
        }
        return true;
    }
}

