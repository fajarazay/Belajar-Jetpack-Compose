package com.github.fajarazay.belajarjetpackcompose.network

import androidx.core.os.BuildCompat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.fajarazay.belajarjetpackcompose.BuildConfig

/**
 * Created by FAJAR SEPTIAN on 27/02/20.
 *
 * @Author Fajar Septian
 * @Email fajarajay10@gmail.com
 * @Github https://github.com/fajarazay
 */
class ApiClient {
    companion object {
        fun getClient() : Retrofit {
            val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL_NEWS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit
        }
    }
}