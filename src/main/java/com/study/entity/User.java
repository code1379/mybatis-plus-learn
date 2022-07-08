package com.study.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
// @TableName("mp_user") // 指定表名为 mp_user
public class User {
    /*
    * 1. MP 默认找 id 作为主键。如果数据库中的 id 改为 user_id，那么我们直接将 这里的 id 改为 userId，
    * 之后执行插入语句是不生效的。因为 MP 不知道主键是那个了。 可以通过 @TableId 来指定主键
    * 2. 有的公司因为某些原因，实体类 和 数据表没有对应关系！！！
    * 比如 表中叫 name，实体中叫 realname。 或者说 我就比较任性，我就想让他们不一样。
    * 可以通过 @TableField('name') 来指定表中对应的 Field
    * */
    // @TableId // 设置主键
    private Long id;
    // @TableField("name") // 设置表中对应的 Field
    private String name;
    private Integer age;
    private String email;
    private Long managerId;
    private Date createTime;
}
