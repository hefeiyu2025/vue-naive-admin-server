package com.naiveadmin.server.security.filter;

import com.naiveadmin.server.security.service.UserDetailsServiceImpl;
import com.naiveadmin.server.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取请求头中的token
        String token = getTokenFromRequest(request);
        
        // 如果token不为空且SecurityContext中没有认证信息
        if (StringUtils.hasText(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 验证token
            if (jwtUtil.validateToken(token)) {
                // 从token中获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                
                // 加载用户详情
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 设置认证信息到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtUtil.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtUtil.getTokenPrefix())) {
            return bearerToken.substring(jwtUtil.getTokenPrefix().length()).trim();
        }
        return null;
    }
}