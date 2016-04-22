package com.zhuke.svmclassifier.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ZHUKE on 2016/4/21.
 */
public class SVMFilter implements Filter {

    // private Logger logger = LogManager.getLogger(SVMFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //logger.info("A new http connect, client host:" + request.getRemoteAddr() + ", request the url:" + ((HttpServletRequest) request).getRequestURL());
        chain.doFilter(request, response);

    }

    public void destroy() {

    }
}
