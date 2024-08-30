package com.example.productmaster.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class ProductServiceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Pre Handle method is Calling");
        log.info("METHOD type: {}", request.getMethod());
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Servlet PATH: {}", request.getServletPath());
        if(handler instanceof HandlerMethod){
            Class<?> controllerClass = ((HandlerMethod) handler).getBeanType();
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            log.info("Controller name: {}", controllerClass.getName());
            log.info("Method name: {}", methodName);
        }
        String path = request.getServletPath();

        System.out.println(path);
        if(path.equals("/index.html") || path.equals("/signup.html")){
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            System.out.println("Session activated by : " + session.getAttribute("username"));
            return true;
        }
        else {
            String ajaxHeader = request.getHeader("X-Requested-With");
            if (ajaxHeader != null && ajaxHeader.equals("XMLHttpRequest")) {
                // Allow certain AJAX requests even if the session is null
                String ajaxPath = request.getServletPath();

                return ajaxPath.equals("/api/login") || ajaxPath.equals("/signup/addUser");
            } else {
                log.warn("\n\n\n**\n\nUser tried to access the page: {} after logging out or without logged in\n\n", request.getServletPath());
                response.sendRedirect("/index");
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Post Handle method is Calling");
        log.info("Response Status: {}", response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Request and Response is completed");
    }
}
