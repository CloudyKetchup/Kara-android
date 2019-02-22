package com.krypt0n.kara.Remote

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private var instance: Retrofit? = null

    fun getInstance(): Retrofit {
        instance = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:3000/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return instance as Retrofit
    }
}
