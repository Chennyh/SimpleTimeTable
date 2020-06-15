package com.chennyh.simpletimetable.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shuang.kou
 */
@Data
@AllArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
}
