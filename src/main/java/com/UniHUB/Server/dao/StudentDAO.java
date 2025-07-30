package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.dto.CourseDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.dto.FeedbackDTO;
import java.util.List;

// DAO Interface
public interface StudentDAO {
    void addQuery(QueryDTO query);
    List<QueryDTO> getQueriesByStudentId(int studentId);
    List<CourseDTO> getCoursesByStudentId(int studentId);
    List<ResourceDTO> getResourcesByCourseId(int courseId);
    void addResource(ResourceDTO resource);
    List<FeedbackDTO> getFeedbackByCourseId(int courseId);
    void addFeedback(FeedbackDTO feedback);
}

// Only the StudentDAO interface remains here. Implementation is now in dao/Impl/StudentDAOImpl.java
