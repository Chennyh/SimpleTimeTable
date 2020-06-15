package com.chennyh.simpletimetable.bean;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;
import lombok.Data;

import java.util.List;

@Data
public class Course implements ScheduleEnable {

    private Long id;

    private Long uid;

    /**
     * 课程名
     */
    private String name;

    //无用数据
    private String time;

    /**
     * 教室
     */
    private String room;

    /**
     * 教师
     */
    private String teacher;

    /**
     * 第几周至第几周上
     */
    private List<Integer> weekList;

    /**
     * 开始上课的节次
     */
    private int start;

    /**
     * 上课节数
     */
    private int step;

    /**
     * 周几上
     */
    private int day;

    /**
     * 课程的颜色
     */
    private int color;

    /**
     * 学期
     */
    private String term;

    public Course() {
    }

    public Course(String name, String time, String room, String teacher, int color) {
        this.name = name;
        this.time = time;
        this.room = room;
        this.teacher = teacher;
        this.color = color;
    }

    public Course(String name, String room, String teacher, List<Integer> weekList, int start, int step, int day, int color, String time) {
        super();
        this.name = name;
        this.room = room;
        this.teacher = teacher;
        this.weekList = weekList;
        this.start = start;
        this.step = step;
        this.day = day;
        this.color = color;
        this.time = time;
    }

    @Override
    public Schedule getSchedule() {
        Schedule schedule = new Schedule();
        schedule.setDay(getDay());
        schedule.setName(getName());
        schedule.setRoom(getRoom());
        schedule.setStart(getStart());
        schedule.setStep(getStep());
        schedule.setTeacher(getTeacher());
        schedule.setWeekList(getWeekList());
        schedule.setColorRandom(getColor());
        return schedule;
    }
}
