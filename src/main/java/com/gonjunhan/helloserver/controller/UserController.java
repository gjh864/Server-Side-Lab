package com.gonjunhan.helloserver.controller;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;
import com.gonjunhan.helloserver.entity.UserInfo;
import com.gonjunhan.helloserver.service.UserService;
import com.gonjunhan.helloserver.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. 注册
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    // 2. 登录
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    // 3. 根据ID查用户
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    // 4. 分页
    @GetMapping("/page")
    public Result<Object> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }

    // 5. 用户详情（带Redis缓存）
    @GetMapping("/detail/{userId}")
    public Result<UserDetailVO> getUserDetail(@PathVariable Long userId) {
        return userService.getUserDetail(userId);
    }

    // 6. 更新用户信息（删除Redis缓存）
    @PutMapping("/{id}/detail")
    public Result<String> updateUserInfo(
            @PathVariable("id") Long userId,
            @RequestBody UserInfo userInfo) {
        userInfo.setUserId(userId);
        return userService.updateUserInfo(userInfo);
    }
}