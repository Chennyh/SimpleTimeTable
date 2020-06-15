package com.chennyh.simpletimetable.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Chennyh
 * @date 2020/6/14 15:22
 */
public class ApiClient {

    private static final String BASE_URL = "http://192.168.1.2:9333/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
