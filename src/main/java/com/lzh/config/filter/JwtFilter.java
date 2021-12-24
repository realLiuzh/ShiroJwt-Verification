package com.lzh.config.filter;

import com.alibaba.fastjson.JSON;
import com.lzh.data.Constant;
import com.lzh.data.JwtToken;
import com.lzh.data.Response;
import com.lzh.exception.CustomExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Jwt过滤器
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 过滤器执行流程：
     * isAccessAllowed()->isLoginAttempt()->executeLogin()
     */

    /**
     * 是否允许访问
     * 逻辑：只有同时满足以下条件才允许该请求访问：
     * 1、请求头中携带token;
     * 2、token经过检验合格(通过自定义Realm的认证);
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否有认证意愿
     * 逻辑：如果请求头中如有token则认为有认证意愿  (一定要把认证和登录概念区分开来)
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(Constant.HTTP_HEADER_TOKEN);
        return token != null;
    }

    /**
     * 执行认证
     * 逻辑：在HttpHeader中获取前端传来的token，并将其封装为JwtToken。然后将JwtToken传给自定义Realm进行认证
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(Constant.HTTP_HEADER_TOKEN);
        JwtToken jwtToken = new JwtToken(token);
        //提交给realm进行认证，如果发生错误，会在realm中抛出异常并捕获
        getSubject(request, response).login(jwtToken);
        //如果没有抛出异常则证明认证成功，返回true
        return true;
    }

    /**
     * 猜测:如果isAccessAllowed()返回false会调用sendChallenge()方法
     * sendChallenge()调用super.Challenge()方法 访问接口会返回401错误
     */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        log.debug("Authentication required: sending 401 Authentication challenge response.");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json;charset=utf-8");
        Response responseData = new Response();
        responseData.setCode(CustomExceptionType.NO_LOGIN.getCode());
        responseData.setMessage(CustomExceptionType.NO_LOGIN.getTypeDesc());

        PrintWriter out = null;
        try {
            out = httpResponse.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = JSON.toJSONString(responseData);
        assert out != null;
        out.print(json);
        out.flush();

        return false;
    }
}
