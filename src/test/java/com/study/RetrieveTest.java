package com.study;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
     * <p>
     * SELECT id,name,age,email,manager_id,create_time FROM user
     * WHERE (date_format(create_time,'%Y-%m-%d') = ? AND
     * manager_id IN (select id from user where name like '王%'))
     */
    @Test
    public void selectListByWrapper4() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2022-07-08").inSql("manager_id", "" +
                "select id from user where name like '王%'");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 需求5
     * 名字为王姓 并且 (年龄小于（我这里加个等于） 40 或 邮箱不为空)
     * name like '王%' and (age <= 40 or email is not null)
     * SELECT id,name,age,email,manager_id,create_time FROM user WHERE (name LIKE ? AND (age <= ? OR email IS NOT NULL))
     * 王%(String), 40(Integer)
     */
    @Test
    public void selectListByWrapper5() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王").and(wq -> wq.le("age", 40).or().isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 需求6
     * 名字为王姓 或者（年龄小于40 并且年龄大于20 并且 邮箱不为空）
     * name like '王%' and (age < 40 and age > 20 and email is not null)
     */
    @Test
    public void selectListByWrapper6() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王")
                // .or(wq -> wq.lt("age", 40)
                //         .and(wq1 -> wq1.gt("age", 20))
                //         .and(wq2 -> wq2.isNotNull("email")));
                .or(wq -> wq.lt("age", 40).gt("age", 20).isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 需求7
     * (年龄小于40 或 邮箱不为空) 并且名字为 王姓
     * (age < 40 or email is not null) and name like "王%"
     * 没有 括号的话， or 的优先级小于 and 的优先级，所以必须要加括号
     * <p>
     * nested https://baomidou.com/pages/10c804/#nested
     */
    @Test
    public void selectListByWrapper7() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        // queryWrapper.lt("age", 40).or().isNotNull("email")
        //         .and(wq -> wq.likeRight("name", "王"));
        // 错误 上面的查询相当于 没加 括号的查询
        queryWrapper.nested(wq -> wq.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 需求8
     * 年龄为 30、31、34、35
     * age in (30, 31, 34, 35)
     */
    @Test
    public void selectListByWrapper8() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35, 40));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 需求9
     * 只返回满足条件的其中一条语句即可
     * limit 1
     * <p>
     * last https://baomidou.com/pages/10c804/#last
     */
    @Test
    public void selectListByWrapper9() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("age", Arrays.asList(18)).last("limit 1");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 需求10
     * 名字中包含 打工仔 并且 年龄小于 40（需求1 加强版）
     * 第一种情况 select id,name from user where name like "%打工仔%" and age < 40
     * 第二种情况 select id,name,age,email from user where name like "%打工仔%" and age < 40
     */
    @Test
    public void selectListByWrapper10() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.select("id", "name")
                .like("name", "打工仔").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectListByWrapper11() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        // 排除字段的写法
        queryWrapper.select(User.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"))
                .like("name", "打工仔").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 什么条件下会使用到 condition 传入 false 呢 ？
     * 前端查询页面，两个查询条件 1. 姓名 2. 邮箱。
     * 当用户点击查询按钮时，这两个条件是可输入可不输入的，都可以
     * <p>
     * 原来的处理方式，是要判断这两个字段是否为非空。
     */
    @Test
    public void testCondition() {
        String name = "王";
        String email = "";
        condition(name, email);
    }

    private void condition(String name, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        // 因为我这里找不到 isNotEmpty 方法，先用 isNotBlank 替代
        // if (StringUtils.isNotBlank(name)) {
        //     queryWrapper.like("name", name);
        // }
        // if (StringUtils.isNotBlank(email)) {
        //     queryWrapper.like("email", email);
        // }
        // 上面写法的简写
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(email), "email", email);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 实体作为条件构造器构造方法的参数
     * SELECT id,name,age,email,manager_id,create_time FROM user WHERE name=? AND age=?
     * */
    @Test
    public void selectByWrapperEntity(){
        User whereUser = new User();
        whereUser.setName("王总");
        whereUser.setAge(40);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(whereUser);
        // 下面和上面是互不干扰的。一定要慎用
        // queryWrapper.like("name", "雨").lt("age", 40);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * allEq https://baomidou.com/pages/10c804/#alleq
     * 1. age 有值，SELECT id,name,age,email,manager_id,create_time FROM user WHERE (name = ? AND age = ?)
     * 2. age = null，SELECT id,name,age,email,manager_id,create_time FROM user WHERE (name = ? AND age IS NULL)
     *    我们可以通过设置第二个参数为 false，将 为 null 的值不添加到查询语句中
     * */
    @Test
    public void selectByWrapperAllEq(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王总");
        params.put("age", null);
        // TODO 1. 将条件加入到 queryWrapper 中
        // queryWrapper.allEq(params, false);

        // TODO 2. 对查询参数继续过滤
        // allEq(BiPredicate<R, V> filter, Map<R, V> params)
        queryWrapper.allEq((k, v) -> !k.equals("name"), params);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
}
