package com.harry.tong.controller;

import com.harry.tong.entity.User;
import com.harry.tong.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {
    @Autowired
    UsersService usersService;

    @GetMapping("/users")
    public List<User> userAll() {
        return usersService.usersAll();
    }
}
