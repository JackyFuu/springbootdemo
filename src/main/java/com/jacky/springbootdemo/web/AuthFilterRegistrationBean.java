package com.jacky.springbootdemo.web;

import com.jacky.springbootdemo.entity.User;
import com.jacky.springbootdemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author jacky
 * @time 2021-01-23 11:06
 * @discription Spring Boot会自动扫描所有的FilterRegistrationBean类型的Bean，
 *              然后，将它们返回的Filter自动注册到Servlet容器中，无需任何配置。
 *
 *              FilterRegistrationBean本身不是Filter，它实际上是Filter的工厂。
 *              Spring Boot会调用getFilter()，把返回的Filter注册到Servlet容器中。
 *              因为我们可以在FilterRegistrationBean中注入需要的资源，
 *              然后，在返回的AuthFilter中，这个内部类可以引用外部类的所有字段，自然也包括注入的UserService，
 *              所以，整个过程完全基于Spring的IoC容器完成。
 */

@Order(10)
@Component
public class AuthFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    @Autowired
    UserService userService;

    @Override
    public Filter getFilter() {
        return new AuthFilter();
    }

    class AuthFilter implements Filter {

        final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            try {
                authenticateByHeader(req);
            } catch (RuntimeException e) {
                logger.warn("login by authorization header failed.", e);
            }
            chain.doFilter(request, response);
        }

        private void authenticateByHeader(HttpServletRequest req) throws UnsupportedEncodingException {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                logger.info("try authenticate by authorization header...");
                String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), StandardCharsets.UTF_8);
                int pos = up.indexOf(':');
                if (pos > 0) {
                    String email = URLDecoder.decode(up.substring(0, pos), String.valueOf(StandardCharsets.UTF_8));
                    String password = URLDecoder.decode(up.substring(pos + 1), String.valueOf(StandardCharsets.UTF_8));
                    User user = userService.signin(email, password);
                    req.getSession().setAttribute(UserController.KEY_USER, user);
                    logger.info("user {} login by authorization header ok.", email);
                }
            }
        }
    }
}
