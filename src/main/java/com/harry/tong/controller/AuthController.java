package com.harry.tong.controller;

import com.harry.tong.entity.User;
import com.harry.tong.service.UsersService;
import com.harry.tong.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    @PostMapping("/authenticate")
    public HashMap<String, String> createToken(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        HashMap<String, String> tokenRole = new HashMap<>();
        UserDetails userDetails = usersService.loadUserByUsername(username);
        tokenRole.put("role",String.valueOf(userDetails.getAuthorities()));
        tokenRole.put("token",jwtUtil.generateToken(userDetails.getUsername()));
        return tokenRole;
    }

    // 允许只有 ADMIN 角色的用户访问
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Welcome, Admin!");
    }

    // 允许只有 USER 角色的用户访问
    @PreAuthorize("hasRole('user')")
    @GetMapping("/user")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Welcome, User!");
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUser( @PathVariable String username) {
            //  处理请求
            return ResponseEntity.ok(usersService.userSave(username));

    }

}
