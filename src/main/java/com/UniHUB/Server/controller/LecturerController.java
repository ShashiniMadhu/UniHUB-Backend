package com.UniHUB.Server.controller;

import com.UniHUB.Server.dto.*;
import com.UniHUB.Server.service.LecturerService;
import com.UniHUB.Server.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lecturer")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"})
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private NotificationService notificationService;

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

    @PostMapping(value = "/assignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ResponseEntity<?> publishAssignment(
              @RequestParam("courseId") Integer courseId,
              @RequestParam("lecturerId") Integer lecturerId,
              @RequestParam("title") String title,
              @RequestParam("description") String description,
              @RequestParam(value = "attachment" , required = false) MultipartFile attachment,
              @RequestParam("date") LocalDate date

    ) {
        try{
            String imageUrl = null;
            if(attachment != null && !attachment.isEmpty()){
                String fileName = System.currentTimeMillis() + "_" + attachment.getOriginalFilename();
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File uploadPath = new File(uploadDir);
                if(!uploadPath.exists()) uploadPath.mkdirs();

                File dest = new File(uploadDir + fileName);
                attachment.transferTo(dest);

                imageUrl = "/uploads/" + fileName;
            }

            AssignmentsDTO assignmentsDTO = new AssignmentsDTO();
            assignmentsDTO.setCourseId(courseId);
            assignmentsDTO.setLecturerId(lecturerId);
            assignmentsDTO.setTitle(title);
            assignmentsDTO.setDescription(description);
            assignmentsDTO.setAttachment(imageUrl);
            assignmentsDTO.setDate(date);

            AssignmentsDTO saved = lecturerService.publishAssignment(assignmentsDTO);
            return ResponseEntity.ok(saved);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/resource" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
       public ResponseEntity<?> publishresource(
               @RequestParam("lecturerId") Integer lecturerId,
               @RequestParam("courseId") Integer courseId,
               @RequestParam("fileName") String fileName,
               @RequestParam(value = "attachment" , required = false) MultipartFile attachment
    ){
        try{
            String imageUrl = null;
            if(attachment != null && !attachment.isEmpty()){
                String fileName1 = System.currentTimeMillis() + "_" + attachment.getOriginalFilename();
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File uploadPath = new File(uploadDir);
                if(!uploadPath.exists()) uploadPath.mkdirs();

                File dest = new File(uploadDir + fileName);
                attachment.transferTo(dest);

                imageUrl = "/uploads/" + fileName;
            }
            ResourceDTO resourceDTO = new ResourceDTO();
            resourceDTO.setLecturerId(lecturerId);
            resourceDTO.setCourseId(courseId);
            resourceDTO.setFileName(fileName);
            resourceDTO.setAttachment(imageUrl);

            ResourceDTO saved = lecturerService.publishresources(resourceDTO);
            return ResponseEntity.ok(saved);

        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{lecturerId}/feedback")
    public ResponseEntity<List<FeedbackDTO>> getFeedback(
            @PathVariable Integer lecturerId
    ) {
        List<FeedbackDTO> feedbacks = lecturerService.getFeedbackForLecturer(lecturerId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{lecturerId}/announcements")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncements(
            @PathVariable Integer lecturerId) {

        List<AnnouncementDTO> announcements =
                lecturerService.getAnnouncementsByLecturerId(lecturerId);

        return ResponseEntity.ok(announcements);
    }

    @PutMapping(
            value = "/{lecturerId}/announcement",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(
            @PathVariable Integer lecturerId,
            @RequestBody AnnouncementDTO announcementDTO
    ) {
        // ensure the path‐ID is applied
        announcementDTO.setLecturerId(lecturerId);
        AnnouncementDTO updated =
                lecturerService.updateAnnouncement(announcementDTO);
        return ResponseEntity.ok(updated);
    }



    @DeleteMapping("/{lecturerId}/announcement/{announcementId}")
    public ResponseEntity<Void> deleteAnnouncement(
            @PathVariable Integer lecturerId,
            @PathVariable Integer announcementId
    ) {
        lecturerService.deleteAnnouncement(announcementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lecturerId}/assignments")
    public ResponseEntity<List<AssignmentsDTO>> getAssignments(@PathVariable Integer lecturerId) {
        List<AssignmentsDTO> assignments = lecturerService.getAssignmentsByLecturer(lecturerId);
        return ResponseEntity.ok(assignments);
    }

    @DeleteMapping("/{lecturerId}/assignment/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Integer lecturerId,
            @PathVariable Integer assignmentId
    ) {
        lecturerService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lecturerId}/resources")
    public ResponseEntity<List<ResourceDTO>> getResources(
            @PathVariable Integer lecturerId) {
        List<ResourceDTO> list = lecturerService.getResources(lecturerId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{lecturerId}/resource/{resourceId}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Integer lecturerId,
            @PathVariable Integer resourceId
    ) {
        lecturerService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lecturerId}/appointments/pending")
    public ResponseEntity<List<AppointmentDTO>> getPendingAppointments(@PathVariable Integer lecturerId) {
        List<AppointmentDTO> pending = lecturerService.getPendingAppointments(lecturerId);
        return ResponseEntity.ok(pending);
    }

    @PutMapping("/{lecturerId}/appointment/{appointmentId}/take")
    public ResponseEntity<AppointmentDTO> takeAppointment(
            @PathVariable Integer lecturerId,
            @PathVariable Integer appointmentId) {
        AppointmentDTO updated = lecturerService.takeAppointment(lecturerId, appointmentId);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{lecturerId}/appointment/{appointmentId}/reject")
    public ResponseEntity<AppointmentDTO> rejectAppointment(
            @PathVariable Integer lecturerId,
            @PathVariable Integer appointmentId) {
        AppointmentDTO rejected = lecturerService.rejectAppointment(lecturerId, appointmentId);
        return ResponseEntity.ok(rejected);
    }

    @GetMapping("/{lecturerId}/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications(
            @PathVariable Integer lecturerId) {

        List<NotificationDTO> notifications =
                notificationService.getNotificationsByUserId(lecturerId);
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/site/announcements")
    public ResponseEntity<List<SiteAnnouncementDTO>> getAllSiteAnnouncements() {
        List<SiteAnnouncementDTO> dtos = lecturerService.getAllSiteAnnouncements();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{lecturerId}/queries")
    public ResponseEntity<List<LecturerQueryDTO>> getQueries(
            @PathVariable Integer lecturerId) {
        List<LecturerQueryDTO> queries = lecturerService.getQueriesForLecturer(lecturerId);
        return ResponseEntity.ok(queries);
    }
    @PostMapping("/{queryId}/reply")
    public ResponseEntity<?> replyToQuery(
            @PathVariable Integer queryId,
            @RequestBody QueryReplyDTO queryReplyDTO) {
        try {
            // Set the query ID from the URL path
            queryReplyDTO.setQueryId(queryId);
            QueryReplyDTO saved = lecturerService.saveQueryReply(queryReplyDTO);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error saving query reply: " + e.getMessage());
        }
    }




}