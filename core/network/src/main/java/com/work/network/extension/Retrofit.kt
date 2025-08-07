package com.work.network.extension

import com.work.network.model.ErrorResponse
import com.work.network.model.ResultResponse
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