package com.naiveadmin.server.service.impl;

import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysUser;
import com.naiveadmin.server.model.LoginParam;
import com.naiveadmin.server.model.LoginResult;
import com.naiveadmin.server.service.IAuthService;
import com.naiveadmin.server.service.ISysUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final ISysUserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret:your-secret-key}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400}")
    private long jwtExpiration;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USER_ID_KEY = "userId";

    @Override
    public LoginResult login(LoginParam loginParam) {
        // 获取用户信息
        SysUser user = userService.getUserByUsername(loginParam.getUsername());
        if (user == null) {
            throw new ServiceException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }

        // 检查用户状态
        if (!user.getStatus()) {
            throw new ServiceException("用户已被禁用");
        }

        // 生成token
        String token = generateToken(user);

        // 获取用户角色
        List<String> roles = userService.getUserRoles(user.getId());

        // 构建返回结果
        LoginResult result = new LoginResult();
        result.setToken(token);
        result.setUserInfo(LoginResult.UserInfo.fromSysUser(user, roles));

        return result;
    }

    @Override
    public boolean logout() {
        SecurityContextHolder.clearContext();
        return true;
    }

    @Override
    public SysUser getCurrentUser() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return null;
        }
        return userService.getById(userId);
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public String generateToken(SysUser user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);
        
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(user.getUsername())
                .claim(USER_ID_KEY, user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public Long validateToken(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(TOKEN_PREFIX.length()))
                    .getBody();

            return claims.get(USER_ID_KEY, Long.class);
        } catch (Exception e) {
            return null;
        }
    }
} 