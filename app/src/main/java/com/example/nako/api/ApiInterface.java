
package com.example.nako.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("{a3bac7e4582c4e9d9caa}/I2790/json/1/5/DESC_KOR={foodName}")
    Call<String> getFoodCalories(@Path("a3bac7e4582c4e9d9caa") String apiKey, @Path("foodName") String foodName);
}
