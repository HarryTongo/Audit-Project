package com.harry.tong.filter;

import com.harry.tong.service.UsersService;
import com.harry.tong.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtRequestFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    public JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    public JwtRequestFilter(JwtUtil jwtUtil, UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorizationHeader = httpRequest.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // 检查 Authorization 头是否包含 "Bearer" 并提取 JWT Token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);    // 从 "Bearer" 后提取 JWT Token
            username = jwtUtil.getUsername(jwtToken);                 // 从 Token 中提取用户名
        }

        // 如果用户名存在并且 SecurityContextHolder 中还没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 验证 JWT Token
            if (jwtUtil.validateToken(jwtToken,username)) {
                // 通过用户名加载用户的详细信息
                UserDetails userDetails = usersService.loadUserByUsername(username);

                // 创建认证对象，并设置到 SecurityContext中
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // 将认证信息设置到当前的 SecurityContext中
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 继续执行过滤器链
        chain.doFilter(request, response);
    }
}
