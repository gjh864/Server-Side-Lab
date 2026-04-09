package com.gonjunhan.helloserver.controller;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;
import com.gonjunhan.helloserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. 注册接口：POST /api/users
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    // 2. 登录接口：POST /api/users/login
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    // 3. 查询用户信息（GET /api/users/{id}）：调用真实数据库逻辑
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    // 4. 分页查询用户列表 - 路径为 GET /api/users/page
    @GetMapping("/page")
    public Result<Object> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }
}