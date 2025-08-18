package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.UserDAO;
import com.UniHUB.Server.dto.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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

    @PersistenceContext
    private EntityManager em;


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
    public Optional<UserDTO> findByResetToken(String token) {
        String sql = "SELECT * FROM user WHERE reset_token = ?";
        try {
            UserDTO user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserDTO.class), token);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(UserDTO user) {
        String sql = "UPDATE user SET password = ?, reset_token = ?, token_expiry = ? WHERE email = ?";
        jdbcTemplate.update(sql,
                user.getPassword(),
                user.getResetToken(),
                user.getTokenExpiry(),
                user.getEmail()
        );
    }





    @Override
    public Integer findStudentIdByUserId(Integer userId) {
        String sql = "SELECT s.student_Id FROM student s " +
                "INNER JOIN user u ON s.user_Id=u.user_Id WHERE u.user_Id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null; // No student found for this user
        }
    }

    @Override
    public Integer findLecturerIdByUserId(Integer userId) {
        String sql= "SELECT l.lecturer_Id FROM lecturer l " +
                "INNER JOIN user u ON l.user_Id=u.user_id WHERE u.user_Id=?";
        try{
            return jdbcTemplate.queryForObject(sql,Integer.class,userId);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }
}
