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
       User user = new User();
       user.setName("UI打工仔");
       user.setAge(18);
       user.setEmail("ui@study.com");
       user.setId(Long.valueOf(6));
       user.setManager_id(Long.valueOf(2));
       int rows = userMapper.insert(user);
        System.out.println("影响记录数:" + rows);
    }
}
