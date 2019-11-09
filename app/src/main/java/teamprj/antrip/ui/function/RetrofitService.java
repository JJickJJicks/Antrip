package teamprj.antrip.ui.function;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import teamprj.antrip.data.model.Directions;

public interface RetrofitService {

    @GET("directions/json")
    public Call<Directions> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );
}
