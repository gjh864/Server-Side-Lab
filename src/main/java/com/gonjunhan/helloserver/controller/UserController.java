package com.gonjunhan.helloserver.controller;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // 1. 查询用户（返回统一 Result）
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        // 故意触发算术异常（测试全局异常拦截）
        int a = 1 / 0;
        return Result.success("查询成功，正在返回 ID 为 " + id + " 的用户信息");
    }

    // 2. 新增用户
    @PostMapping
    public Result<String> createUser(@RequestBody User user) {
        return Result.success("新增成功，接收到用户：" + user.getName() + "，年龄：" + user.getAge());
    }

    // 3. 更新用户
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return Result.success("更新成功，ID " + id + " 的用户已修改为：" + user.getName());
    }

    // 4. 删除用户
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long id) {
        return Result.success("删除成功，已移除 ID 为 " + id + " 的用户");
    }
}