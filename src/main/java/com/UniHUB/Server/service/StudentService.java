package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.dto.QueryDTO;

public interface StudentService {
    void addFeedback(FeedbackDTO feedback);
    boolean updateQuery(QueryDTO queryDTO);
}
