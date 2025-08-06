package com.work.profile_service.data.service.api

import com.work.profile_service.data.model.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileService {
    @GET("/api/")
    suspend fun getProfile(): Response<ProfileResponse>
}