package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class StudentServiceImpl implements StudentService {



    @Autowired
    private StudentDAO studentDAO;



    @Override
    public void addFeedback(FeedbackDTO feedback) {

            // You can add validation or business logic here before saving
            if (feedback.getRate() < 1 || feedback.getRate() > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }


        studentDAO.addFeedback(feedback);



    }

    @Override
    public boolean updateQuery(QueryDTO queryDTO) {

        try {

            Timestamp createdAt = studentDAO.getQueryCreatedTime(queryDTO.getQueryId());
            if (createdAt == null) {
                throw new IllegalArgumentException("Query not found");
            }

            // Check 2-minutes limit
            Instant twoMinutesLater = createdAt.toInstant().plusSeconds(120);
            if (Instant.now().isAfter(twoMinutesLater)) {
                throw new IllegalArgumentException("Query can only be updated within 2 minutes of creation");
            }

            return studentDAO.updateQuery(queryDTO);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
