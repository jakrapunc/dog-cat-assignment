package com.work.profile_service

import com.work.network.base.ApiManager
import com.work.profile_service.data.model.response.ProfileResponse
import com.work.profile_service.data.service.api.ProfileService
import com.work.profile_service.data.service.repository.remote.ProfileRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class ProfileRemoteTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockApiManager: ApiManager

    @MockK
    private lateinit var mockProfileService: ProfileService

    private lateinit var profileRemote: ProfileRemote

    @Before
    fun setUp() {
        val mockRetrofit = mockk<retrofit2.Retrofit>() // Create a mock for Retrofit

        every {
            mockApiManager.init(
                baseUrl = "https://randomuser.me/",
                converter = any(GsonConverterFactory::class)
            )
        } returns mockRetrofit

        every {
            mockRetrofit.create(ProfileService::class.java)
        } returns mockProfileService


        profileRemote = ProfileRemote(mockApiManager)
    }

    @Test
    fun `getProfile should call ProfileService getProfile and return its successful response`() = runTest {
        //Given
        val expectedApiResponse = ProfileResponse(results = listOf(),)
        val successResponse: Response<ProfileResponse> = Response.success(expectedApiResponse)

        coEvery { mockProfileService.getProfile() } returns successResponse

        //When
        val actualResponse = profileRemote.getProfile()

        //Then
        coVerify(exactly = 1) { mockProfileService.getProfile() }

        Assert.assertEquals(successResponse, actualResponse)
        Assert.assertEquals(expectedApiResponse, actualResponse.body())
    }

    @Test
    fun `getProfile should handle error response from ProfileService`() = runTest {
        //Given
        val errorResponseBody = "{\"error\":\"Not found\"}".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse: Response<ProfileResponse> = Response.error(404, errorResponseBody)

        coEvery { mockProfileService.getProfile() } returns errorResponse

        //When
        val actualResponse = profileRemote.getProfile()

        //Then
        coVerify(exactly = 1) { mockProfileService.getProfile() }
        Assert.assertEquals(errorResponse, actualResponse)
        Assert.assertEquals(404, actualResponse.code())
        Assert.assertEquals(false, actualResponse.isSuccessful)
    }

    @Test
    fun `getProfile should propagate exceptions from ProfileService`() = runTest {
        //Given
        val expectedException = RuntimeException("Network error")
        coEvery { mockProfileService.getProfile() } throws expectedException

        var actualException: Throwable? = null

        //When
        try {
            profileRemote.getProfile()
        } catch (e: Throwable) {
            actualException = e
        }

        //Then
        coVerify(exactly = 1) { mockProfileService.getProfile() }
        Assert.assertEquals(expectedException, actualException)
    }

    @Test
    fun `getProfileService should initialize ProfileService with correct parameters`() {
        val service = profileRemote.getProfileService()

        verify(exactly = 1) {
            mockApiManager.init(
                baseUrl = "https://randomuser.me/",
                converter = any(GsonConverterFactory::class)
            )
        }

        Assert.assertEquals(
            mockProfileService,
            service
        )
    }
}