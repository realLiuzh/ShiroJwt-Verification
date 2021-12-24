package com.lzh.config.shiro;

import com.lzh.data.Constant;
import com.lzh.data.JwtToken;
import com.lzh.mapper.RoleMapper;
import com.lzh.mapper.UserMapper;
import com.lzh.model.dto.RoleDto;
import com.lzh.model.dto.UserDto;
import com.lzh.model.po.User;
import com.lzh.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

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
