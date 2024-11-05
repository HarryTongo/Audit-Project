package com.harry.tong.config;

import com.harry.tong.filter.JwtRequestFilter;
import com.harry.tong.service.UsersService;
import com.harry.tong.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
// 开启允许你在方法调用之前或者之后进行权限校验
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置身份验证管理器,指定了用户详细信息服务和密码编码器
        auth.userDetailsService(usersService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 CSRF（跨站请求伪造）保护，通常用于 API
        http.cors().and().csrf().disable()
                // 允许你定义应用程序中哪些 URL 路径可以被哪些用户或角色访问
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()   // 允许公开访问的路径
                // 所有其他请求必须经过身份验证
                .anyRequest().authenticated()
                .and()
                // 添加自定义的 JWT 过滤器，以便处理 JWT 验证
                .addFilterAfter(new JwtRequestFilter(jwtUtil,usersService), UsernamePasswordAuthenticationFilter.class);
    }

    // 显式定义 AuthenticationManager Bean
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    // 使用 BCryptPasswordEncoder 来加密和验证用户密码。
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
