package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dto.AssignmentsDTO;
import com.UniHUB.Server.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerDAO lecturerDAO;

    @Override
    public AnnouncementDTO publishAnnouncement(AnnouncementDTO announcementDTO) {
        return lecturerDAO.saveAnnouncement(announcementDTO);
    }

    @Override
    public AssignmentsDTO publishAssignment(AssignmentsDTO assignmentsDTO){
        return lecturerDAO.saveAssignments(assignmentsDTO);
    }
}