package com.UniHUB.Server.controller;

import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/lecturer")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"})
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @PostMapping(value = "/announcement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> publishAnnouncement(
            @RequestParam("lecturerId") Integer lecturerId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("content") String content,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment
    ) {
        try {
            String imageUrl = null;
            if (attachment != null && !attachment.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + attachment.getOriginalFilename();
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) uploadPath.mkdirs();

                File dest = new File(uploadDir + fileName);
                attachment.transferTo(dest);

                imageUrl = "/uploads/" + fileName; // Or full path: "http://localhost:8086/uploads/..."
            }

            AnnouncementDTO announcementDTO = new AnnouncementDTO();
            announcementDTO.setLecturerId(lecturerId);
            announcementDTO.setCourseId(courseId);
            announcementDTO.setContent(content);
            announcementDTO.setLink(link);
            announcementDTO.setAttachment(imageUrl);

            AnnouncementDTO saved = lecturerService.publishAnnouncement(announcementDTO);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}