package com.gonjunhan.helloserver.service.impl;

import com.gonjunhan.helloserver.common.JwtUtil;  // 👈 这个必须加！
import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.common.ResultCode;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.service.UserService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    // 模拟数据库存储
    private static final Map<String, String> userDb = new HashMap<>();

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 校验用户名是否已存在
        if (userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        // 存入模拟数据库
        userDb.put(userDTO.getUsername(), userDTO.getPassword());
        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 校验用户是否存在
        if (!userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        // 2. 校验密码是否正确
        String dbPassword = userDb.get(userDTO.getUsername());
        if (!dbPassword.equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        // 3. 登录成功 → 生成 Token
        String token = JwtUtil.generateToken(userDTO.getUsername());
        return Result.success("Bearer " + token);
    }
}