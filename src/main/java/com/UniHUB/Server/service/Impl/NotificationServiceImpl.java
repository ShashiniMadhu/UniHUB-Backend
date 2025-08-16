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
    public List<NotificationDTO> getNotificationsByUserId(Integer userId) {
        return lecturerDAO.findByUserId(userId);
    }
}