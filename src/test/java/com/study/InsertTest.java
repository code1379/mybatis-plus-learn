package com.study;

import com.study.entity.User;
import com.study.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InsertTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert(){
        /*
        * 1. id 不设置，默认使用雪花算法自增id
        * 2. 实体类字段驼峰，可以对应到数据库里面 _ ， managerId => manager_id
        * 3. 实体类 User 可以对应到 数据库的 user(User) 表。也可以驼峰转带下划线的
        * */
       User user = new User();
       user.setName("富二代打工仔");
       user.setAge(18);
       user.setEmail("rich@study.com");
       // user.setId(Long.valueOf(7)); // 不设置 id，MP 会根据雪花算法自增 id
       user.setManagerId(Long.valueOf(2)); // 这里的 ManagerId 是驼峰，数据库表里是 manager_id
       int rows = userMapper.insert(user);
        System.out.println("影响记录数:" + rows);
    }
}
