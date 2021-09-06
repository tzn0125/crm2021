package com.bjpowernode.crm.web.Filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String servletPath = httpServletRequest.getServletPath();

        if("/login.jsp".equals(servletPath) || "/settings/user/login.do".equals(servletPath)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else {
            HttpSession session = httpServletRequest.getSession();
            User user = (User)session.getAttribute("user");
            //如果user不为null,说明登录过
            if(user != null){
                filterChain.doFilter(httpServletRequest,httpServletResponse);
            }else{
                //重定向到登录页
                httpServletResponse.sendRedirect( httpServletRequest.getContextPath()+ "/login.jsp");
            }
        }
    }
}
