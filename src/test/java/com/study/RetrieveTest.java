package com.study;

import com.study.entity.User;
import com.study.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 查询 retrieve
@SpringBootTest
public class RetrieveTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    // 通过 id 查询
    public void selectById() {
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE id=?
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test
    // 通过 id 数组 查询 使用 Arrays.asList
    public void selectBatchIds() {
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE id IN ( ? , ? )
        List<Long> ids = Arrays.asList(1L, 2L);
        List<User> userList = userMapper.selectBatchIds(ids);
        userList.forEach(System.out::println);
    }

    @Test
    // 通过 条件 进行查询。 对象相当于
    public void selectByMap() {
        Map<String, Object> columnsMap = new HashMap<>();
        // map.put("name", "老板");
        // map.put("age", 18);
        // where name = '老板' and age = 18
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE name = ? AND age = ?
        columnsMap.put("name", "老板");
        columnsMap.put("age", 50);
        List<User> userList = userMapper.selectByMap(columnsMap);
        userList.forEach(System.out::println);
    }

}
