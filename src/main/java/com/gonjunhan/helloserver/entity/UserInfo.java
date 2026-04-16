package com.gonjunhan.helloserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户扩展信息表实体
 */
@Data
@TableName("user_info")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String realName;
    private String phone;
    private String address;
    private Long userId;
}