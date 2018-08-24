package com.android.lazyloading.recyclerview.retrofit;


import com.android.lazyloading.recyclerview.models.Proficiency;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExerciseService {

    String BASE_URL = "https://dl.dropboxusercontent.com";

    @GET("/s/2iodh4vg0eortkl/facts.json")
    Call<Proficiency> getFactsFromApi();
}
