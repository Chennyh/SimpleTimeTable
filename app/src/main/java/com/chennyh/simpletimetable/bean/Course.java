package com.chennyh.simpletimetable.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;
import lombok.Data;

import java.util.List;

@Data
public class Course implements ScheduleEnable, Parcelable {

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

    protected Course(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            uid = null;
        } else {
            uid = in.readLong();
        }
        name = in.readString();
        time = in.readString();
        room = in.readString();
        teacher = in.readString();
        start = in.readInt();
        step = in.readInt();
        day = in.readInt();
        color = in.readInt();
        term = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (uid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uid);
        }
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(room);
        dest.writeString(teacher);
        dest.writeInt(start);
        dest.writeInt(step);
        dest.writeInt(day);
        dest.writeInt(color);
        dest.writeString(term);
    }
}
