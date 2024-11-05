package com.harry.tong.service;

import com.harry.tong.entity.User;
import com.harry.tong.mapper.RoleMapper;
import com.harry.tong.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    RoleMapper roleMapper;

    public List<User> usersAll() {
        List<User> users = usersMapper.selectAll();
        return users;
    }

    public User userSave(String users) {
        return usersMapper.selectSave(users);
    }

    public Set<String> getRolesByUserId(Long userId) {
        return roleMapper.getRolesByUserId(userId);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userSave(username);
        System.out.println(user.getPassword());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()     //  使用 Spring Security 提供的 User 构建器来构造 UserDetails 对象
                .username(user.getUsername())       // 设置用户名
                .password(user.getPassword())       // 设置用户密码
                .authorities(getRolesByUserId(user.getId()).stream().map(role -> "ROLE_" + role).toArray(String[]::new))    // 从数据库中获取用户角色，并将角色名称转换为 Spring Security 所需的格式（以 "ROLE_" 开头）
                .build();
    }
}
