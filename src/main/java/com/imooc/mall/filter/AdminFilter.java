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
 * 功能：校验管理员身份;
 */
public class AdminFilter implements Filter {
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
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        //如果获取不到，说明当前没有用户登录；就返回【用户未登录】的信息
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            //直接return的意思是，直接结束方法；不会执行后面的内容了；（自然，这儿直接结束方法的结果就是：这个请求不会进入Controller）
            return;
        }

        //校验当前用户是否是管理员用户
        boolean isAdminRole = userService.checkAdminRole(currentUser);

        if (isAdminRole) {//如果是管理用户；就放行这个请求；执行【chain.doFilter(request, response);】会把这个请求放行到下一个过滤器或者Controller
            chain.doFilter(request, response);
        } else {//如果不是管理员用户；就没有放行这个请求，而是给出一个错误提示；
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            //这儿的return，是可以不添加的；因为，如果方法执行到这儿的时候，就已经结束了；
            return;
        }
    }


    @Override
    public void destroy() {
    }
}