package com.github.fajarazay.belajarjetpackcompose.helper

import androidx.compose.effectOf
import androidx.compose.memo
import androidx.compose.onCommit
import androidx.compose.state
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Created by FAJAR SEPTIAN on 27/02/20.
 *
 * @Author Fajar Septian
 * @Email fajarajay10@gmail.com
 * @Github https://github.com/fajarazay
 */

//https://stackoverflow.com/questions/59096435/using-livedata-as-state-inside-jetpack-compose-functions
object GetObserveValue {
        fun <T> observe(data: LiveData<T>) = effectOf<T?> {
            val result = +state<T?> { data.value }
            val observer = +memo { Observer<T> { result.value = it } }

            +onCommit(data) {
                data.observeForever(observer)
                onDispose { data.removeObserver(observer) }
            }

            result.value
        }

}