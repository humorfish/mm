package com.you.mm.bean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by you on 2016/11/24.
 */
public interface GankApi {
    @GET("/data/福利/" + DrakeetFactory.meiziSize + "/{page}")
    Observable<Object> getMeiziData(@Path("page") int page);

    @GET("/day/{year}/{month}/{day}")
    Observable<Object> getGankData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("/data/休息视频/" + DrakeetFactory.meiziSize + "/{page}")
    Observable<Object> get休息视频Data(@Path("page") int page);
}
