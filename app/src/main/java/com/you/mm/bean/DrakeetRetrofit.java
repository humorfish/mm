package com.you.mm.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by you on 2017/2/18.
 */

public class DrakeetRetrofit
{
    final GankApi gankService;
    final DrakeetApi drakeetService;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public DrakeetRetrofit()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (DrakeetFactory.isDebug)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(logging);
        }

        httpClient.connectTimeout(12, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = httpClient.build();

        Retrofit.Builder reBuilder = new Retrofit.Builder();
        reBuilder.baseUrl("http://gank.io/api/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit gankRest = reBuilder.build();
        reBuilder.baseUrl("https://leancloud.cn:443/1.1/classes/");
        Retrofit drakeetRest = reBuilder.build();
        gankService = gankRest.create(GankApi.class);
        drakeetService = drakeetRest.create(DrakeetApi.class);
    }

    public GankApi getGankService()
    {
        return gankService;
    }

    public DrakeetApi getDrakeetService()
    {
        return drakeetService;
    }
}
