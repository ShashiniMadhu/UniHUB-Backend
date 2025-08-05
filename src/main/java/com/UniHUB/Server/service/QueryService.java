package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.QueryDTO;
import java.util.List;

public interface QueryService {
    void submitQuery(QueryDTO query);
    List<QueryDTO> getStudentQueries(int studentId);
    // Add more methods as needed
}

