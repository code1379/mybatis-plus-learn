DROP TABLE IF EXISTS user;
CREATE TABLE user(
    id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
    name VARCHAR(30) DEFAULT NULL COMMENT '姓名',
    age INT(11) default NULL COMMENT  '年龄',
    email VARCHAR(50) default NULL COMMENT '邮箱',
    manager_id BIGINT(20) DEFAULT NULL COMMENT '直属上级id',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT manager_fk FOREIGN KEY (manager_id) REFERENCES user(id)
);


DELETE FROM user;

INSERT INTO user (id, name, age, email, manager_id) VALUES
(1, "老板", 50, 'boss@study.com', null),
(2, "王总", 40, 'wangzong@study.com',1),
(3,"前端打工仔", 18, 'front@study.com', 2),
(4,"后端打工仔", 18, 'back@study.com', 2),
(5,"行政打工仔", 18, 'admin@study.com', 2);

