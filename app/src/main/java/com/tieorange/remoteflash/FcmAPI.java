package com.tieorange.remoteflash;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by root on 1/14/17.
 */

public interface FcmAPI {
//    @Headers({"Content-Type: application/json", "Authorization: key=" + Constants.SERVICE_KEY})
    @POST("fcm/send")
    @Headers({"charset: UTF-8", "Authorization: key=" + Constants.SERVICE_KEY})
    Call<FcmResponse> send(@Body String body);
}
