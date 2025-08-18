package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.UserDAO;
import com.UniHUB.Server.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            UserDTO user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserDTO.class), email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer findStudentIdByUserId(Integer userId) {
        String sql = "SELECT s.student_id FROM student s " +
                "INNER JOIN user u ON s.user_id=u.user_id WHERE u.user_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null; // No student found for this user
        }
    }

    @Override
    public Integer findLecturerIdByUserId(Integer userId) {
        String sql= "SELECT l.lecturer_id FROM lecturer l " +
                "INNER JOIN user u ON l.user_id=u.user_id WHERE u.user_id=?";
        try{
            return jdbcTemplate.queryForObject(sql,Integer.class,userId);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }
}
