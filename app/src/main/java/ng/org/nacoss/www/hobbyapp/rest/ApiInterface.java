package ng.org.nacoss.www.hobbyapp.rest;

import ng.org.nacoss.www.hobbyapp.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by root on 4/14/17.
 */

public interface ApiInterface {

    /**
     *
     *
     * Defines a method signature to simulate the HTTP POST request with @POST annotation
     * @param  username
     * @param email
     * @param    password
     *
     */

    @POST("/api/hobby")
    @FormUrlEncoded
    Call<User> registerUser(@Field("username") String username, @Field("email") String email,@Field("password") String password);



    /**
     *
     *
     * Defines a method signature to simulate the HTTP GET request with @GET annotation
     * @param  username
     * @param password
     *
     */
    @GET("/api/hobby/")
    Call<User> signIn(@Query("username") String username, @Query("password") String password);

    /**
     *
     *
     * Defines a method signature to simulate the HTTP PUT request with @PUT annotation
     * @param  username
     * @param  hobby
     *
     */
    @PUT("/api/hobby/{username}")
    @FormUrlEncoded
    Call<User> addHobbyToUser(@Path("username") String username,@Field("hobby") String hobby);

    @GET("/api/hobby/{username}")
    Call<User> getHobbies(@Path("username") String username);
}
