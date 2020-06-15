package com.chennyh.simpletimetable.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @author Chennyh
 * @date 2020/6/14 23:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int code;
    private int status;
    private String message;
    private String path;
    private String timestamp;
    private HashMap<String, Object> errorDetail = new HashMap<>();
}
