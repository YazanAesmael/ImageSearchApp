package com.example.idea.model

import com.example.idea.network.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getQueryImages(
        @Query("q") query:String,
        @Query("key") apiKey:String,
        @Query("image_type") imageType:String
    ):PixabayResponse


}