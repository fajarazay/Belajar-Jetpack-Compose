package com.github.fajarazay.belajarjetpackcompose.network

import com.github.fajarazay.belajarjetpackcompose.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by FAJAR SEPTIAN on 27/02/20.
 *
 * @Author Fajar Septian
 * @Email fajarajay10@gmail.com
 * @Github https://github.com/fajarazay
 */
interface ApiInterface {

    @GET("v2/top-headlines?country=id")
    fun getNews(@Query("apiKey") apiKey: String) : Call<NewsResponse>

}