package com.github.fajarazay.belajarjetpackcompose.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: ArrayList<ArticlesItem> = arrayListOf(),

	@field:SerializedName("status")
	val status: String? = null
)