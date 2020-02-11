package com.victor.nuvem.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.DELETE




class RetrofitInitializer {

    companion object {

        fun getRetrofitInstance(path : String) : Retrofit {
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

interface RetrofitActions {

    // GET TASKS
    @GET("tasks")
    fun getTasks() : Call<List<ModelTask>>

    // POST TASKS
    @FormUrlEncoded
    @POST("tasks")
    fun postTask(
        @Field("id") id: Int,
        @Field("task_title") task_title: String,
        @Field("complete") complete: Boolean
    ): Call<ModelTask>

    // PUT TASKS
    @FormUrlEncoded
    @PUT("tasks/{id}")
    fun putTask(
        @Path("id") id: Int,
        @Field("task_title") task_title: String,
        @Field("complete") complete: Boolean
    ): Call<ModelTask>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: Int): Call<ModelTask>
}