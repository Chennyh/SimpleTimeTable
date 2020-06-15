package com.chennyh.simpletimetable.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Chennyh
 * @date 2020/6/14 17:00
 */
@Data
@AllArgsConstructor
public class RegisterUserRequests {
    private String email;
    private String username;
    private String password;
}
