package com.work.profile_service

import com.work.profile_service.data.model.response.ProfileResponse
import com.work.profile_service.data.service.repository.IProfileRepository
import com.work.profile_service.data.service.repository.ProfileRepository
import com.work.profile_service.data.service.repository.remote.IProfileRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


@ExperimentalCoroutinesApi
class ProfileRepositoryTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockProfileRemote: IProfileRemote

    private lateinit var profileRepository: IProfileRepository

    @Before
    fun setUp() {
        profileRepository = ProfileRepository(mockProfileRemote)
    }

    @Test
    fun `getProfile should return data from remote when createCall is successful`() = runTest {
        // Given
        val mockProfileData = ProfileResponse(results = listOf())
        val successResponse: Response<ProfileResponse> = Response.success(mockProfileData)

        coEvery { mockProfileRemote.getProfile() } returns successResponse

        // When
        val resultFlow = profileRepository.getProfile()
        val result = resultFlow.first() // Collect the first (and only) emission for this NBR

        // Then
        coVerify(exactly = 1) { mockProfileRemote.getProfile() }
        Assert.assertEquals(mockProfileData, result)
    }

    @Test
    fun `getProfile should throw exception when remote returns unsuccessful response without body`() = runTest {
        // Given
        val errorResponse: Response<ProfileResponse> = Response.error(
            404,
            "Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )
        coEvery { mockProfileRemote.getProfile() } returns errorResponse

        // When & Then
        try {
            profileRepository.getProfile().first() // Trigger the flow and collection
            fail("Expected an exception to be thrown for unsuccessful response")
        } catch (e: Exception) {
            Assert.assertTrue(e is Throwable) // Be more specific if NBR defines it
        }
        coVerify(exactly = 1) { mockProfileRemote.getProfile() }
    }

    @Test
    fun `getProfile should throw exception when remote call itself fails`() = runTest {
        // Given
        val networkException = RuntimeException("Network connection failed")
        coEvery { mockProfileRemote.getProfile() } throws networkException

        // When & Then
        try {
            profileRepository.getProfile().first()
            fail("Expected a RuntimeException to be thrown")
        } catch (e: RuntimeException) {
            Assert.assertEquals(networkException.message, e.message)
        }
        coVerify(exactly = 1) { mockProfileRemote.getProfile() }
    }
}