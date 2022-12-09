package com.imooc.mall.filter;

import com.imooc.mall.common.Constant;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述：过滤器类;
 * 功能：校验用户身份;
 */
public class UserFilter implements Filter {

    public static User currentUser;

    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //首先，获取Session对象；以便可以尝试从Session对象中，获取当前登录用户；
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();


        //尝试获取当前登录用户
        currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        //如果获取不到，说明当前没有用户登录；就返回【用户未登录】的信息
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN测试的中文\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            //直接return的意思是，直接结束方法；不会执行后面的内容了；（自然，这儿直接结束方法的结果就是：这个请求不会进入Controller）
            return;
        }

        //如果当前有用户登录，那么就放行
        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
    }
}