package com.study;

import com.study.entity.User;
import com.study.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect(){
        System.out.println("----- selectAll method test ------");
        List<User> userList = userMapper.selectList(null);
        Assertions.assertEquals(userList.size(), 5);
        userList.forEach(System.out::println);
    }
}
