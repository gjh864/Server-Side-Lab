package com.gonjunhan.helloserver.service;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);

    // 新增：根据 ID 查询用户（必须在接口里先声明！）
    Result<User> getById(Long id);
}