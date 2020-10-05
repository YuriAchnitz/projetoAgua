package com.yach.projetoagua.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProjetoAguaApi {

    @GET("updatetl/all/all")
    Call<List<Post>> getPost();

    @GET("updatetl/{cep}/all")
    Call<List<Post>> getAllDate(@Path("cep") String location);

    @GET("updatetl/all/{date}")
    Call<List<Post>> getAllCep(@Path("date") String date);

    @GET("updatetl/{cep}/{date}")
    Call<List<Post>> getAllCustom(@Path("cep") String cep, @Path("date") String date);
}
