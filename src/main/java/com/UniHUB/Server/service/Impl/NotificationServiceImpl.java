package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dto.NotificationDTO;
import com.UniHUB.Server.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private LecturerDAO lecturerDAO;



    @Override
    public List<NotificationDTO> getNotificationsByLecturerId(Integer lecturerId) {
        // First, get the userId corresponding to the lecturerId
        Integer userId = lecturerDAO.findUserIdByLecturerId(lecturerId);

        if (userId == null) {
            throw new RuntimeException("No user found for lecturer ID: " + lecturerId);
        }

        // Then, get notifications for that userId
        return lecturerDAO.findNotificationsByUserId(userId);
    }


}