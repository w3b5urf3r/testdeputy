package com.deputy.test.mariolopez.api;

import com.deputy.test.mariolopez.beans.Shift;
import com.deputy.test.mariolopez.beans.ShiftInfo;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Completable;
import rx.Observable;

/**
 * Created by mario on 23/02/2017.
 */

public interface DeputyRestApi {

    @GET("shifts")
    Observable<List<Shift>> getShifts();

    @POST("shift/start")
    Completable startShift(@Body ShiftInfo shiftInfo);

    @POST("shift/end")
    Completable endShift(@Body ShiftInfo shiftInfo);

}
