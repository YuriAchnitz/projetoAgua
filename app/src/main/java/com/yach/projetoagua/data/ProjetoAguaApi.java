package com.yach.projetoagua.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProjetoAguaApi {

    @GET("updatetl/{cep}/{type}/{date}")
    Call<List<Post>> getAllCustom(@Path("cep") String cep, @Path("type") String type, @Path("date") String date);

    @GET("ws/{cep}/json/")
    Call<List<Post>> getCep(@Path("cep") String cep);

    @POST("report")
    Call<Post> createPost(@Body Post post);

    /*
    @POST("report")
    Call<Post> createPost(@Body Post post);
     */
}
