package com.chennyh.simpletimetable.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chennyh
 * @date 2020/6/14 17:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseRquest {
    private Long userId;
    private String name;
    private String time;
    private String room;
    private String teacher;
    private List<Integer> weekList;
    private int start;
    private int step;
    private int day;
    private String term;
    private int color;
}
