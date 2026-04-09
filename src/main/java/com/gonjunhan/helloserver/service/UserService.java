package com.gonjunhan.helloserver.service;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);

    // 新增：根据 ID 查询用户
    Result<User> getById(Long id);

    // 新增：获取用户分页数据（按截图要求）
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);
}