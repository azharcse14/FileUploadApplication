package com.azhar.fileuploadapplication.model.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(Url.baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val API_SERVICE: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}