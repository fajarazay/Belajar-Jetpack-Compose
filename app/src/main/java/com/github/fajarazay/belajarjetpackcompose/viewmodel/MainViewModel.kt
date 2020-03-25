package com.github.fajarazay.belajarjetpackcompose.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.fajarazay.belajarjetpackcompose.BuildConfig
import com.github.fajarazay.belajarjetpackcompose.model.ArticlesItem
import com.github.fajarazay.belajarjetpackcompose.model.NewsResponse
import com.github.fajarazay.belajarjetpackcompose.network.ApiClient
import com.github.fajarazay.belajarjetpackcompose.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by FAJAR SEPTIAN on 27/02/20.
 *
 * @Author Fajar Septian
 * @Email fajarajay10@gmail.com
 * @Github https://github.com/fajarazay
 */


class MainViewModel : ViewModel() {
    val apikey = BuildConfig.API_KEY

    var listNews = MutableLiveData<ArrayList<ArticlesItem>>()


    fun setListNews() {
        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<NewsResponse> = apiInterface.getNews(apikey)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>?, t: Throwable?) {
                Log.d("onFailure: ", t?.message.toString())
            }

            override fun onResponse(call: Call<NewsResponse>?, response: Response<NewsResponse>) {

                listNews.value = (response.body()?.articles)
                Log.d("listNews: ", listNews.toString())
            }
        })
    }

    internal fun getListNews(): LiveData<ArrayList<ArticlesItem>> {
        return listNews
    }
}