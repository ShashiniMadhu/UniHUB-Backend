package com.UniHUB.Server.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryReplyDTO {

    @JsonProperty("query_id")
    private int queryId;
    @JsonProperty("reply_id")
    private int replyId;
    private String reply;
}
