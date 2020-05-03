package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.UserDto;
import com.delta.kylintask.entity.UserInfo;
import com.delta.kylintask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private LdapTemplate ldapTemplate;

    private String ldapDomainName;

    @Override
    public ServerResponse<UserInfo> login(UserDto userDto) {
        //这里注意用户名加域名后缀  userDn格式：anwx@minibox.com
        String userDn = userDto.getUsername() + ldapDomainName;
        //token过期时间 4小时
        Date tokenExpired = new Date(new Date().getTime() + 60*60*4*1000);
        DirContext ctx = null;
        try {
            //使用用户名、密码验证域用户
            ctx = ldapTemplate.getContextSource().getContext(userDn, userDto.getPassword());
            //如果验证成功根据sAMAccountName属性查询用户名和用户所属的组

            //使用Jwt加密用户名和用户所属组信息

            /*String compactJws = Jwts.builder()
                    .setSubject(employee.getName())
                    .setAudience(employee.getRole())
                    .setExpiration(tokenExpired)
                    .signWith(SignatureAlgorithm.HS512, jwtKey).compact();*/
            //登录成功，返回客户端token信息。这里只加密了用户名和用户角色，而displayName和tokenExpired没有加密
            UserInfo userInfo = new UserInfo();
            return ServerResponse.createBySuccess(userInfo);
        } catch (Exception e) {
            //登录失败，返回失败HTTP状态码
            return ServerResponse.createByErrorCodeMessage(HttpStatus.UNAUTHORIZED.value(), "未授权用户");
        } finally {
            //关闭ldap连接
            try {
                ctx.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ServerResponse<UserInfo> logout(UserDto userDto) {
        //set token expired
        return null;
    }
}
