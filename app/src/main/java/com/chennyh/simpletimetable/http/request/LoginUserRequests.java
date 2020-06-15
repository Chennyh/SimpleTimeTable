package com.chennyh.simpletimetable.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Chennyh
 * @date 2020/6/14 15:17
 */
@Data
@AllArgsConstructor
public class LoginUserRequests {
    private String username;
    private String password;
    private boolean rememberMe;
}
