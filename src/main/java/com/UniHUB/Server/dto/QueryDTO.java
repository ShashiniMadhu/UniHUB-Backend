package com.UniHUB.Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryDTO {
    private int queryId;
    private int courseId;
    private int studentId;
    private String question;
    private String category;
    private String priority;
    private Timestamp createdAt;
}
