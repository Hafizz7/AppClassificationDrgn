package com.example.classificationdragon

import com.example.classificationdragon.models.BeritaTerbaru

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BeritaApi {
    @GET("v2/everything")
    fun getNews(
        @Query("q") query: String,
        @Query("language") language: String = "id",
        @Query("apiKey") apiKey: String
    ): Call<BeritaTerbaru>
}
