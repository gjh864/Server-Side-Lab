package com.gonjunhan.helloserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.common.ResultCode;
import com.gonjunhan.helloserver.dto.UserDTO;
import com.gonjunhan.helloserver.entity.User;
import com.gonjunhan.helloserver.entity.UserInfo;
import com.gonjunhan.helloserver.mapper.UserMapper;
import com.gonjunhan.helloserver.mapper.UserInfoMapper;
import com.gonjunhan.helloserver.security.JwtUtil;
import com.gonjunhan.helloserver.service.UserService;
import com.gonjunhan.helloserver.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String CACHE_KEY_PREFIX = "user:detail:";

    @Override
    public Result<String> register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
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

    // ===================== 【正确 JWT 登录】 =====================
    @Override
    public Result<String> login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        if (!user.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        // 生成 JWT
        String token = jwtUtil.generateToken(user.getUsername());
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

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;

        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isBlank()) {
            try {
                UserDetailVO vo = cn.hutool.json.JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(vo);
            } catch (Exception e) {
                redisTemplate.delete(key);
            }
        }

        UserDetailVO detail = userMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        redisTemplate.opsForValue().set(key, cn.hutool.json.JSONUtil.toJsonStr(detail), 10, TimeUnit.MINUTES);

        return Result.success(detail);
    }

    // ===================== 【正确更新 + 删除缓存】 =====================
    @Override
    @Transactional
    public Result<String> updateUserInfo(UserInfo userInfo) {
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.error("参数错误");
        }

        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userInfo.getUserId());
        int rows = userInfoMapper.update(userInfo, wrapper);

        if (rows > 0) {
            String key = CACHE_KEY_PREFIX + userInfo.getUserId();
            redisTemplate.delete(key);
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
}