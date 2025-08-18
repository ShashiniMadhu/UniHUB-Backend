package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.CourseFullDTO;
import com.UniHUB.Server.dto.SiteAnnouncementDTO;
import com.UniHUB.Server.dto.UserDTO;

import java.util.List;

public interface AdminDAO {
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    List<UserDTO> viewStudents();

    List<UserDTO> viewLecturers();

    SiteAnnouncementDTO createSiteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO);

    List<SiteAnnouncementDTO> viewAnnouncements();

    SiteAnnouncementDTO deleteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO);

    SiteAnnouncementDTO updateAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO);

    UserDTO deactivateUser(UserDTO userDTO);

    UserDTO reactivateUser(UserDTO userDTO);

    UserDTO getUserById(Integer userId);

    CourseFullDTO createCourse(CourseFullDTO courseFullDTO);

    List<UserDTO> getAvailableLecturers();

    void assignLecturerToCourse(int lecturerId, int courseId);

}
