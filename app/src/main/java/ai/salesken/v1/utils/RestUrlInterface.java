package ai.salesken.v1.utils;


import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestUrlInterface {


    @GET("v1/leads/get_task_history")
    Call<ArrayList<HashMap<String, String>>> getTaskHistory(@Query("userID") String userID);

    @GET("v1/leads/get_task_history_incomplete")
    Call<ArrayList<HashMap<String, String>>> getIncompleteTask(@Query("userID") String userID);



    @GET("v1/task/create_task")
    Call<Integer> create_task(@Query("user") String user, @Query("toNumber") String toNumber);


}