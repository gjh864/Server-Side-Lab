package com.gonjunhan.helloserver.service;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;
import com.gonjunhan.helloserver.entity.UserInfo;
import com.gonjunhan.helloserver.vo.UserDetailVO;

public interface UserService {

    // 注册
    Result<String> register(UserDTO userDTO);

    // 登录
    Result<String> login(UserDTO userDTO);

    // 根据ID查询用户
    Result<User> getById(Long id);

    // 分页查询
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);

    // 查询用户详情（带Redis缓存）
    Result<UserDetailVO> getUserDetail(Long userId);

    // 更新用户信息（删除缓存）
    Result<String> updateUserInfo(UserInfo userInfo);
}