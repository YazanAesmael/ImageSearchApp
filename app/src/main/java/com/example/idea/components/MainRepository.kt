package com.example.idea.components

import com.example.idea.model.ApiService
import com.example.idea.network.PixabayResponse
import com.example.idea.util.Constant
import com.example.idea.util.Resource
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService:ApiService) {
    suspend fun getQueryItems(q:String):Resource<PixabayResponse>{
        return try{
            val result = apiService.getQueryImages(query = q, apiKey = Constant.KEY, imageType = "photo")
            Resource.Success(data = result)
        }catch (e:Exception){
            Resource.Error(message = e.message.toString())
        }
    }
}