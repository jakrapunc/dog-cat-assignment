package com.work.profile_service.domain

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateFormatUseCase {
    fun convert(inputDate: String): String? {
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )

        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = inputFormat.parse(inputDate)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            return date?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            e.printStackTrace() // Handle parsing exception
            return null
        }
    }

}