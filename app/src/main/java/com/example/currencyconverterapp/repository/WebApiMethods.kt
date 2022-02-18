package com.example.currencyconverterapp.repository

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface WebApiMethods {

    @GET()
    suspend fun getApiResponseAsync(@Url apiUrl : String, @QueryMap params : Map<String,String>) :Response<ResponseBody>

}

