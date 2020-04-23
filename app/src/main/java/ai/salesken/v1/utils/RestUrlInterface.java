package ai.salesken.v1.utils;


import java.util.ArrayList;
import java.util.HashMap;

import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.TaskSubmission;
import ai.salesken.v1.pojo.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestUrlInterface {


    @POST("authenticate")
    Call<SaleskenResponse> autenticate(@Body User user);

    @GET("v1/task/upcoming")
    Call<SaleskenResponse> upcoming(@Header("Authorization")String token);

    @GET("v1/task/recent")
    Call<SaleskenResponse> recent(@Header("Authorization")String token);

    @GET("v1/task/completed")
    Call<SaleskenResponse> completed(@Header("Authorization")String token);

    @GET("v1/task/create_task")
    Call<Integer> create_task(@Query("user") String user, @Query("toNumber") String toNumber);

    @POST("v1/password/forgot")
    Call<SaleskenResponse> forgot_password(@Body User user);

    @POST("v1/password/reset")
    Call<SaleskenResponse> reset_password(@Body User user);

    @POST("v1/password/change")
    Call<SaleskenResponse> change_password(@Header("Authorization")String token,@Body User user);


    @PUT("v1/user/update/user")
    Call<SaleskenResponse> updateUser(@Header("Authorization")String token,@Body User user);

    @Multipart
    @POST("v1/user/image/upload")
    Call<SaleskenResponse> uploadImage(@Header("Authorization")String token, @Part MultipartBody.Part file, @Part("file") RequestBody requestBody);

    @POST("v1/task/dispostion")
    Call<SaleskenResponse> disposition(@Header("Authorization")String token, @Body TaskSubmission taskSubmission);

    @GET("v1/task/stages_list/{task_id}")
    Call<SaleskenResponse> stage_list(@Header("Authorization")String token, @Path("task_id") Integer task_id );
}