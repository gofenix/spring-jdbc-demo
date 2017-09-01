# 简介
Spring操作数据库主要是通过JdbcTemplate，写了一个最小可行的应用，记录一下。github的地址是：https://github.com/zhenfeng-zhu/spring-jdbc-demo

使用到的jar包：
>spring-context

>spring-jdbc

>common-dbcp

pom.xml文件如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zzf.springjdbcdemo</groupId>
    <artifactId>springjdbcdemo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.10.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>4.3.10.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>commons-dbcp</groupId>
            <artifactId>commons-dbcp</artifactId>
            <version>1.4</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
        </dependency>
    </dependencies>


</project>
```

这个demo的主要是连接mysql，对user表进行增删改查。

首先在本地的mysql中简历users表：
```sql
/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-01 23:02:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------

```
新建实体类 User.java:
```java
package com.demo.entity;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
```

新建UserDao.java如下：
```java
import com.demo.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addUser(User user){
        String sql="insert into users(id,name,password,email,phone) values (?,?,?,?,?)";
        Object[] params=new Object[]{user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getPhone()};
        jdbcTemplate.update(sql, params);
    }
}
```
然后在resources文件夹下建一下jdbc的配置文件，jdbc.properties。
```properties
jdbc.url=jdbc:mysql://localhost:3306/demo
jdbc.driverName=com.mysql.jdbc.Driver
jdbc.username=root
jdbc.password=root
```
然后在resources文件夹下创建applicationContext.xml。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:jdbc.properties" />
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
          p:username="${jdbc.username}"
          p:password="${jdbc.password}"
          p:url="${jdbc.url}"
          p:driverClassName="${jdbc.driverName}" />

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate"
          p:dataSource-ref="dataSource" />

    <bean id="userDao" class="com.demo.dao.UserDao"
          p:jdbcTemplate-ref="jdbcTemplate" />
</beans>
```
## 测试
创建UserTest.java类来运行：
```java
package com.demo.test;

import com.demo.dao.UserDao;
import com.demo.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserTest {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserDao userDao= (UserDao) context.getBean("userDao");
        User user=new User();
        user.setId(1);
        user.setName("lucas");
        user.setEmail("example@163.com");
        user.setPassword("******");
        user.setPhone("123456");
        userDao.addUser(user);
    }
}
```
可以看到已经插入进入了。
