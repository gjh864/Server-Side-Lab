package com.gonjunhan.helloserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 新增这一行导入
import com.gonjunhan.helloserver.common.JwtUtil;
import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.common.ResultCode;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;
import com.gonjunhan.helloserver.mapper.UserMapper;
import com.gonjunhan.helloserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user);
        return Result.success("注册成功！");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        if (!user.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        String token = JwtUtil.generateToken(userDTO.getUsername());
        return Result.success("Bearer " + token);
    }

    @Override
    public Result<User> getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success(user);
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userMapper.selectPage(page, null);
        return Result.success(userPage);
    }

}