# Shiro-Jwt整合

Github项目地址：[https://github.com/realLiuzh/ShiroJwt-Verification](https://github.com/realLiuzh/ShiroJwt-Verification)

## 特性

- SpringBoot整合Shiro-Jwt实现RBAC(Role-Based-Access-Control)。
- 整合腾讯云SMS服务，实现手机号登录注册。
- Validation实现参数校验。
- 跨域支持。

## 前置知识

- [深入理解Jwt](https://blog.csdn.net/weixin_45921593/article/details/122046842?spm=1001.2014.3001.5501)
- [JWT(JSON Web Token) 原理简析](https://juejin.cn/post/7002904348212592654)
- 蓝山工作室-失物招领模块

## 接口文档

[https://www.showdoc.com.cn/1772458942730902/8234809986254447](https://www.showdoc.com.cn/1772458942730902/8234809986254447)

## 准备Maven工程

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.lzh</groupId>
    <artifactId>ShiroJwt-Verification</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>ShiroJwt-Verification</name>
    <description>ShiroJwt-Verification</description>

    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>2.3.7.RELEASE</spring-boot.version>
    </properties>

    <dependencies>

        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!--腾讯云-->
        <dependency>
            <groupId>com.tencentcloudapi</groupId>
            <artifactId>tencentcloud-sdk-java</artifactId>
            <version>3.1.270</version><!-- 注：这里只是示例版本号（可直接使用），可获取并替换为 最新的版本号，注意不要使用4.0.x版本（非最新版本） -->
        </dependency>

        <!--加密解密类-->
        <dependency>
            <groupId>org.jasypt</groupId>
            <artifactId>jasypt</artifactId>
            <version>1.9.3</version>
        </dependency>

        <!--validator-->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.3.6.Final</version>
        </dependency>

        <!--jwt-->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.12.0</version>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.3</version>
        </dependency>

        <!--fastjson-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.76</version>
        </dependency>

        <!--shiro-spring-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.7.0</version>
        </dependency>

        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.6</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.3.7.RELEASE</version>
                <configuration>
                    <mainClass>com.lzh.ShiroJwtVerificationApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <id>repackage</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>

```

## JwtUtil



```java
package com.lzh.util;

/**
 * Jwt工具类
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
public class JwtUtil {

    /**
     * 生成token
     * claim:
     * username -> RealUsername
     * exp      -> 有效时间5天
     * 加密算法   -> HMAC256
     */
    public static String getToken(String username) {
        //username + 私钥 = 最终的secret
        Algorithm algorithm = Algorithm.HMAC256(username + Constant.JWT_PRIVATE_KEY);
        return JWT.create()
                //jwt payload
                .withClaim(Constant.JWT_CLAIM_KEY, username)
                //jwt 有效时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(Constant.JWT_EXPIRE_TIME) * 1000))
                .sign(algorithm);
    }

    /**
     * 检验token
     */
    public static void verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getClaim(token, Constant.JWT_CLAIM_KEY) + Constant.JWT_PRIVATE_KEY);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
    }

    /**
     * 获得Token中的荷载信息  (无需解码)
     */
    public static String getClaim(String token, String claimKey) {
        return JWT.decode(token).getClaim(claimKey).asString();
    }
}

```

## Shiro组件

### JwtToken

```java
package com.lzh.data;


/**
 * 自定义JwtToken
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */

@AllArgsConstructor
public class JwtToken implements AuthenticationToken {

    private String token;

    /**
     * 注意：实现的两个方法全部返回token。(对于理解ShiroJwt整合很重要)
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

```



### JwtFilter

```java
package com.lzh.config.filter;

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

```

### CustomRealm

```java
package com.lzh.config.shiro;

/**
 * 自定义Realm
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
public class CustomRealm extends AuthorizingRealm {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;


    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    /**
     * 认证
     * 这里认证并非真正的登录认证，shiro与jwt整合，登录还是由Service层负责
     * 而这里的认证承担的是校验Token的责任
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getPrincipal();

        JwtUtil.verifyToken(token);

        String username = JwtUtil.getClaim(token, Constant.JWT_CLAIM_KEY);
        /**
         * Q:这里的认证异常 AuthenticationException 抛给了谁？
         * A:层层调用后，抛给了JwtFilter的isAccessAllowed()方法 最终结果是isAccessAllowed()返回false。
         * Q:异常信息是什么?
         * A:异常信息是sendChallenge()中写死的方法，这里(包括JwtUtil中)抛出的认证异常仅仅起标识作用
         */
        if (username == null) {
            throw new AuthenticationException();
        }
        User user = userMapper.selectByUsername(username);

        if (user == null) {
            throw new AuthenticationException();
        }
        /**
         * param1:真实的用户名
         * param2:真实的密码
         * param3:该realm名称
         *
         * Shiro将param2与token中的credentials比较，如果相等则验证通过
         * 在这里比较结果恒为true
         */
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String username = JwtUtil.getClaim(principalCollection.toString(), Constant.JWT_CLAIM_KEY);

        UserDto userDto = userMapper.selectRoleByUserName(username);

        if (userDto.getRole() == null)
            return simpleAuthorizationInfo;

        RoleDto roleDto = roleMapper.selectPermissionByRoleId(userDto.getRole().getId());

        /**
         * 这里模拟的用户只有一个身份
         * 有多个身份调用addRoles()
         */
        simpleAuthorizationInfo.addRole(userDto.getRole().getName());
        simpleAuthorizationInfo.addStringPermission(roleDto.getPermission().getPerCode());
        return simpleAuthorizationInfo;
    }


}

```

## ShiroConfig

```java
package com.lzh.config.shiro;

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
        map.put("/user/usernameLogin", "anon");
        map.put("/user/phoneLogin", "anon");
        map.put("/user/register", "anon");
        map.put("/user/sendVerCode","anon");
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

```



## 其他一些工具类

### 加密工具类

```java
package com.lzh.util;


/**
 * 加密工具类
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
public class EncryptUtil {

    /**
     * sh1加密方法加密
     */
    public static String encryptor(String input) {
        BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
        return basicPasswordEncryptor.encryptPassword(input);
    }

    /**
     * 检查密码的合法性
     */
    public static void checkPassword(String inputPassword, String realPassword) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        AssertUtil.isTrue(!passwordEncryptor.checkPassword(inputPassword, realPassword), CustomExceptionType.ACCOUNT_ERROR);
    }
}

```

### 短信工具类

```java
package com.lzh.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;

import static com.lzh.data.Constant.*;

/**
 * 短信发送工具类
 *
 * @author 志昊的刘
 * @date 2021/12/24
 */
public class SmsUtil {

    public static boolean sendSms(String phone, String captcha) {
        try {

            Credential cred = new Credential(SMS_SECRET_ID, SMS_SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();

            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象
            SendSmsRequest req = new SendSmsRequest();

            /**
             * 填充请求参数,这里request对象的成员变量即对应接口的入参
             */

            /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
            req.setSmsSdkAppid(SMS_SDK_APP_ID);

            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看 */
            req.setSign(SMS_SIGN_NAME);

            /* 模板 ID: 必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看 */
            req.setTemplateID(SMS_TEMPLATE_ID);

            /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             * 例如+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            phone="86"+phone;
            String[] phoneNumbers = {phone};
            req.setPhoneNumberSet(phoneNumbers);

            /* 模板参数: 若无模板参数，则设置为空 */
            String[] templateParamSet = {captcha};
            req.setTemplateParamSet(templateParamSet);

            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse res = client.SendSms(req);

            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));

            // 也可以取出单个值，你可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
            System.out.println(res.getRequestId());


        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
```











