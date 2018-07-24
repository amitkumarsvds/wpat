package com.proficiency.exercise.wpat;


import java.util.List;

import models.exercisemodels.Proficiency;
import models.exercisemodels.Row;
import retrofit.ExerciseInterface;
import retrofit.FactsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseServiceCommunicator {

    public static String mGetTitle;

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

                listener.onFailure("Invalid Response..!");

            }
        });
    }


    interface OnServiceCallFinishedListener {

        void onSuccess(final List<Row> respRows, String title);

        void onFailure(String failureData);
    }

}


