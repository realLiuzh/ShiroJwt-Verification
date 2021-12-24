package com.lzh.config.shiro;

import com.lzh.config.filter.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Configuration
public class ShiroConfig {

    /**
     * 将realm注入容器中
     */
    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

    /**
     * 使得securityManager生效
     */
    @Bean
    public DefaultWebSecurityManager manager(@Qualifier("customRealm") CustomRealm customRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //关联Realm(使CustomRealm生效)
        manager.setRealm(customRealm);

        //关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    /**
     * 重要:配置过滤器和拦截路径
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("manager") DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(manager);

        //添加过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwtFilter", new JwtFilter());
        factoryBean.setFilters(filters);

        //设置拦截路径 (与JwtFilter配合食用)
        Map<String, String> map = new HashMap<>();
        map.put("/user/login", "anon");
        map.put("/user/register", "anon");
        map.put("/**", "jwtFilter");

        factoryBean.setFilterChainDefinitionMap(map);

        //factoryBean.setUnauthorizedUrl("/user/unauth");
        return factoryBean;
    }


    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("manager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


}
