package com.demo.dao;

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
