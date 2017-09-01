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
