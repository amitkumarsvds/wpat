package com.android.lazyloading.recyclerview.lazyload;


import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.models.Row;
import com.android.lazyloading.recyclerview.retrofit.ExerciseInterface;
import com.android.lazyloading.recyclerview.retrofit.FactsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LazyLoadViewServiceCommunicator {

    private String mGetTitle;

    /**
     * api call to fetch facts from server through retrofit API communication
     */
    void serviceFacts(final OnServiceCallFinishedListener listener) {
        ExerciseInterface apiService =
                FactsApi.getClient().create(ExerciseInterface.class);

        Call<Proficiency> call = apiService.getFactsFromApi();
        call.enqueue(new Callback<Proficiency>() {
            @Override
            public void onResponse(Call<Proficiency> call, Response<Proficiency> response) {

                if (response.body() == null) {
                    listener.onFailure("No data available");
                } else {
                    mGetTitle = response.body().getTitle();

                    listener.onSuccess(response.body().getRows(), mGetTitle);
                }
            }

            @Override
            public void onFailure(Call<Proficiency> call, Throwable t) {

                listener.onFailure("Invalid Response.. Please try after sometime!");

            }
        });
    }


    interface OnServiceCallFinishedListener {

        void onSuccess(final List<Row> respRows, String title);

        void onFailure(String failureData);
    }

}


