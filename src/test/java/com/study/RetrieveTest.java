package com.study;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    /*
     * 下面都是条件查询构造器 Wrapper
     * 双击 Shift 查询 AbstractWrapper。QueryWrapper 是 AbstractWrapper 的子类
     * */
    @Test
    public void selectListByWrapper() {
        // 名字中包含 打工仔 且 年龄小于 40
        // name like "%打工仔%" and age < 40
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE (name LIKE ? AND age < ?)
        // Parameters: %打工仔%(String), 40(Integer)

        // * QueryWrapper 是 AbstractWrapper 的一个子类
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name", "打工仔").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    @Test
    public void selectListByWrapper2() {
        // 名字中包含 打工仔 且 （年龄大于等于 20 且 小于等于 40）并且 email 不为空
        // name like "%打工仔%" and age between 20 and 40 and email is not null
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE (name LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)
        // %打工仔%(String), 20(Integer), 40(Integer)
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name", "打工仔").between("age", 20, 40).isNotNull("email");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectListByWrapper3() {
        // 名字中为王姓 *或者* 年龄大于等于 40，按照年龄降序排列，年龄相同的按照 id 升序排列

        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE (name LIKE ? AND age >= ?) ORDER BY age DESC,id ASC
        // %王%%(String), 40(Integer)
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王").or().ge("age", 40).orderByDesc("age").orderByAsc("id");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 需求4
     * 创建日期为 2022年7月8号 并且 直属上级的名字为 王姓
     * <p>
     * dateformat(create_time, '%Y-%m-%d') and manager_id in (select id from user where name like '王%')
     * 条件构造器： apply https://baomidou.com/pages/10c804/#apply
     *
     *  SELECT id,name,age,email,manager_id,create_time FROM user
     *  WHERE (date_format(create_time,'%Y-%m-%d') = ? AND
     *  manager_id IN (select id from user where name like '王%'))
     */
    @Test
    public void selectListByWrapper4() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2022-07-08").inSql("manager_id", "" +
                "select id from user where name like '王%'");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
}
