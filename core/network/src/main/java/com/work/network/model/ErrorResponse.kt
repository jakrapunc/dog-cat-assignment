package com.work.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorResponse(
    val message: String? = null,
    val exception: Exception? = null,
    val code: Int? = null,
    val status: String? = null,
    val title: String? = null,
    val systemMessage: String? = null,
    val date: String? = null,
) : Parcelable
