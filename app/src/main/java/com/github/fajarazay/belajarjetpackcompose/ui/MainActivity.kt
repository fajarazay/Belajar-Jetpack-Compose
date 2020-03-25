package com.github.fajarazay.belajarjetpackcompose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.ui.core.*
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import com.github.fajarazay.belajarjetpackcompose.helper.DateTime
import com.github.fajarazay.belajarjetpackcompose.helper.GetObserveValue
import com.github.fajarazay.belajarjetpackcompose.model.ArticlesItem
import com.github.fajarazay.belajarjetpackcompose.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var news: MutableLiveData<ArrayList<ArticlesItem>> = MutableLiveData()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.setListNews()

        mainViewModel.getListNews().observe(this, Observer {
            news.postValue(it)
        })

        setContent {
            NewsApp()
        }
    }

    @Composable
    fun NewsApp() {
        MaterialTheme {

            Column {
                TopAppBar(
                    title = { Text(text = "Belajar Jetpack Compose") }
                )
                VerticalScroller(modifier = Flexible(1f)) {
                    Column(modifier = Spacing(32.dp)) {
                        NewsList()
                    }
                }
            }
        }
    }

    @Composable
    fun NewsList() {
        GetObserveValue.observe(news).unaryPlus()

        news.value?.iterator()?.forEach { data ->
            Card(
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
                modifier = Spacing(bottom = 16.dp)
            ) {
                Container(expanded = true, alignment = Alignment.Center) {
                    Row(
                        modifier = ExpandedWidth,
                        arrangement = Arrangement.SpaceBetween

                    ) {

                        val image = data.urlToImage?.let {
                            // +image(it)
                            +image(it)
                        }
                        if (image != null) {
                            Container(expanded = true, width = 150.dp, height = 180.dp) {
                                Clip(shape = RoundedCornerShape(4.dp)) {
                                    DrawImage(image)
                                }
                            }
                        }

                        Column(modifier = Spacing(16.dp)) {
                            FlexColumn(
                                crossAxisSize = LayoutSize.Expand,
                                mainAxisAlignment = MainAxisAlignment.End,
                                crossAxisAlignment = CrossAxisAlignment.Stretch
                            )
                            {
                                // Center {
                                data.title?.let {
                                    Text(
                                        text = it, maxLines = 2,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                                data.description?.let {
                                    Text(
                                        text = it, maxLines = 3,
                                        modifier = Spacing(top = 8.dp),
                                        style = TextStyle(
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                                data.author?.let {
                                    Text(
                                        text = it,
                                        modifier = Spacing(top = 8.dp),

                                        style = TextStyle(
                                            fontSize = 14.sp
                                        )
                                    )
                                }

                                data.publishedAt?.let {
                                    Text(
                                        text = DateTime.getFormatDate(it),
                                        modifier = Spacing(top = 8.dp),

                                        style = TextStyle(
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}