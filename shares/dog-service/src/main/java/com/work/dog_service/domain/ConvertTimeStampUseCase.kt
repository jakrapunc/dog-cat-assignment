package com.work.dog_service.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConvertTimeStampUseCase {
    fun invoke(time: Long): String {
        val date = Date(time)

        val sdf = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        )

        return sdf.format(date)
    }
}