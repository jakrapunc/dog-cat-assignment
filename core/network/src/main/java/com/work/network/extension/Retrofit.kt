package com.work.network.extension

import com.kabigon.weatherforecast.data.base.model.ErrorResponse
import com.kabigon.weatherforecast.data.base.model.ResultResponse
import retrofit2.Response

fun <T> Response<T>.toNetworkResult(): ResultResponse<T> {
    try {
        if (this.isSuccessful && this.body() != null) {
            return ResultResponse.Success(this.body())
        } else {
            val errorBody = this.errorBody()?.string()
            val errorMessage = if (errorBody.isNullOrEmpty()) {
                this.message()
            } else {
                "unknown error"
//                Gson().fromJson(errorBody, ApiError::class.java).message
            }

            return ResultResponse.Error(
                ErrorResponse(
                    message = errorMessage
                )
            )
        }
    } catch(e: Exception) {
        return ResultResponse.Error(
            ErrorResponse(
                message = "unknown error"
            )
        )
    }
}