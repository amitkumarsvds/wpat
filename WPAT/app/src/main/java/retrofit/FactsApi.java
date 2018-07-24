package retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FactsApi {
    public static final String BASE_URL = "https://dl.dropboxusercontent.com";
    private static Retrofit retrofit = null;

    private FactsApi() {
        // private constructor
    }

    /**
     * global retrofit instance
     *
     * @return retrofit
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
