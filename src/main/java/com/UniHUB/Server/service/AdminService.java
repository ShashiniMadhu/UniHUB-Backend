package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.SiteAnnouncementDTO;
import com.UniHUB.Server.dto.UserDTO;

import java.util.List;

public interface AdminService {

    /**
     * create users(Lecturer/Student)
     * @param userDTO
     * @return
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * view all students details
     * @return
     */
    List<UserDTO> viewStudents();

    List<UserDTO> viewLecturers();

    SiteAnnouncementDTO createSiteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO);

    List<SiteAnnouncementDTO> viewAnnouncements();

    SiteAnnouncementDTO deleteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO);

    SiteAnnouncementDTO updateAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO);

    UserDTO deactivateUser(UserDTO userDTO);

    UserDTO reactivateUser(UserDTO userDTO);

}
