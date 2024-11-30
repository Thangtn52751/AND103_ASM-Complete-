package dev.md19303.and103_asmcompleted.Services;

import java.util.List;

import dev.md19303.and103_asmcompleted.Model.AuthResponse;
import dev.md19303.and103_asmcompleted.Model.Cake;
import dev.md19303.and103_asmcompleted.Model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Part;

public interface APIService {
    String DOMAIN = "http://192.168.1.8:3000"; // Make sure this is the correct base URL

    // Endpoint for User Authentication
    @POST("/register")
    @Headers("Content-Type: application/json")
    Call<Void> register(@Body User user);

    @POST("/login")
    @Headers("Content-Type: application/json")
    Call<AuthResponse> login(@Body User user);

    @GET("/")
    Call<List<Cake>> getCake();

    @POST("/add_cake")
    @Headers("Content-Type: application/json")
    Call<Void> addCake(@Body Cake cake);

    @GET("/delete/{id}")
    Call<Void> deleteCake(@Path("id") String id);

    @PUT("/update/{id}")
    @Headers("Content-Type: application/json")
    Call<Void> updateCake(@Path("id") String id, @Body Cake updatedCake);

    @GET("/search")
    Call<List<Cake>> searchCakes(@Query("name") String name);

    @GET("/product/{id}")
    Call<Cake> getCakeDetails(@Path("id") String id);
}
