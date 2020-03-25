package com.github.fajarazay.belajarjetpackcompose.helper

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by FAJAR SEPTIAN on 28/02/20.
 *
 * @Author Fajar Septian
 * @Email fajarajay10@gmail.com
 * @Github https://github.com/fajarazay
 */
object DateTime {
    fun getFormatDate(date: String?): String {
        return formatDate(date, "EEEE, dd MMM yyyy")
    }

    private fun formatDate(date: String?, format: String): String {
        var result = ""

        val old = SimpleDateFormat("yyyy-MM-dd")
        try {
            val oldDate = old.parse(date)
            val newFormat = SimpleDateFormat(format)
            result = newFormat.format(oldDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result
    }

}
