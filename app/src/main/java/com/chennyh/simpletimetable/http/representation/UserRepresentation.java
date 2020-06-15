package com.chennyh.simpletimetable.http.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRepresentation {
    private Long id;
    private String username;
    private String email;
}
