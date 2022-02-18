package com.example.currencyconverterapp.repository

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkController(private val context: Context, baseUrl: String?) : Interceptor {

    private var retrofit: Retrofit

    init {

        val client = OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .followRedirects(false)
                .followSslRedirects(false)
                .build()

        /// creating retrofit instance.
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl(baseUrl)
                .client(client)
                .build()

    }


    fun <T> initAPIService(service: Class<T>?): T {
        return retrofit.create(service)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder().headers(getJsonHeader()).build()
        return chain.proceed(request)
    }

    private fun getJsonHeader(): Headers {
        val builder: Headers.Builder = Headers.Builder()
        builder.add("Content-Type", "application/json")
        builder.add("Accept", "application/json")
        return builder.build()
    }

}