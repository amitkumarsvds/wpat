package retrofit;

import models.exercisemodels.Proficiency;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExerciseInterface {
    @GET("/s/2iodh4vg0eortkl/facts.json")
    Call<Proficiency> getFactsFromApi();
}
