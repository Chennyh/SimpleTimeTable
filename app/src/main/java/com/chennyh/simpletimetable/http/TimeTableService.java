package com.chennyh.simpletimetable.http;

import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.http.representation.UserRepresentation;
import com.chennyh.simpletimetable.http.request.AddCourseRquest;
import com.chennyh.simpletimetable.http.request.LoginUserRequests;
import com.chennyh.simpletimetable.http.request.RegisterUserRequests;
import com.chennyh.simpletimetable.http.request.UserUpdateRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

/**
 * @author Chennyh
 * @date 2020/6/14 0:05
 */
public interface TimeTableService {

    @POST("api/auth/login")
    Call<ResponseBody> loginUser(@Body LoginUserRequests loginUserRequests);

    @GET("api/users/i")
    Call<UserRepresentation> getUserInfo(@Header("Authorization") String authorization);

    @POST("api/users")
    Call<ResponseBody> registerUser(@Body RegisterUserRequests registerUserRequests);

    @PUT("api/users")
    Call<ResponseBody> updateUser(@Header("Authorization") String authorization, @Body UserUpdateRequest userUpdateRequest);

    @POST("api/courses")
    Call<ResponseBody> addCourse(@Header("Authorization") String authorization, @Body AddCourseRquest addCourseRquest);

    @GET("api/courses/{userId}")
    Call<ArrayList<Course>> getCoursesById(@Header("Authorization") String authorization, @Path("userId") Long userId, @Query("today") boolean today);

    @DELETE("api/courses/{courseId}")
    Call<ResponseBody> deleteCourse(@Header("Authorization") String authorization, @Path("courseId") Long coursesId);

    @PUT("api/courses/{courseId}")
    Call<ResponseBody> updateCourse(@Header("Authorization") String authorization, @Path("courseId") Long coursesId, @Body AddCourseRquest addCourseRquest);

}
