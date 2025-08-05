package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.QueryDTO;
import java.util.List;

// DAO Interface
public interface StudentDAO {
    void addQuery(QueryDTO query);
    List<QueryDTO> getQueriesByStudentId(int studentId);
}

// Only the StudentDAO interface remains here. Implementation is now in dao/Impl/StudentDAOImpl.java
