package com.gonjunhan.helloserver.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 标识这是一个配置类，Spring 启动时会自动加载
@Configuration
public class MyBatisPlusConfig {

    /**
     * 注册 MyBatis-Plus 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加分页插件，并指定底层数据库类型为 PostgreSQL
        // DbType.PostgreSQL 是关键，保证分页语法（limit offset）正确
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));

        return interceptor;
    }
}