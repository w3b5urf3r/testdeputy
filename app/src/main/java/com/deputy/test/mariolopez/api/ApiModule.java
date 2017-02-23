package com.deputy.test.mariolopez.api;

import android.support.annotation.NonNull;

import com.deputy.test.mariolopez.BuildConfig;
import com.deputy.test.mariolopez.beans.Shift;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mario on 23/02/2017.
 */

/**
 * This class returns to you the interface for the rest api
 * it's done in a ways it can be used through dagger for Dependency Injection
 * and allows us to not stick to any tech in particularly (retrofit)
 */
public class ApiModule {

    public static DeputyRestApi provideDeputyRestApi() {
        OkHttpClient client = getHttpClient();
        Gson gson = new GsonBuilder().setDateFormat(Shift.DATE_FORMAT).create();

        return new Retrofit.Builder()
                .baseUrl("https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .validateEagerly(BuildConfig.DEBUG)
                .build()
                .create(DeputyRestApi.class);
    }

    @NonNull
    private static OkHttpClient getHttpClient() {
        return new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
//                    for sake of simplicity we don't have an auth layer'
                    .addHeader("Authorization", "addb47291ee169f330801ce73520b96f2eaf20ea")
                    .build();
            return chain.proceed(request);
        }).build();
    }
}
