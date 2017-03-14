package com.you.mm.bean;

import com.you.mm.bean.data.MeizhiData;
import com.you.mm.bean.data.休息视频data;
import com.you.mm.bean.entity.GankData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by you on 2016/11/24.
 */
public interface GankApi {

    @GET("data/福利/" + DrakeetFactory.meizhiSize + "/{page}")
    Observable<MeizhiData> getMeizhiData(@Path("page") int page);

    @GET("day/{year}/{month}/{day}") Observable<GankData> getGankData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    @GET("data/休息视频/" + DrakeetFactory.meizhiSize + "/{page}")
    Observable<休息视频data> get休息视频Data(@Path("page") int page);

}