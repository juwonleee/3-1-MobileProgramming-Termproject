package com.example.nako;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroitAPI {
    @GET("/api/d7c3792966e74169a44c/I2790/json/1/10/DESC_KOR={DESC_KOR}")
    Call<ResponseModel> getData(@Path("DESC_KOR") String DESC_KOR);
}
