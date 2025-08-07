package com.work.profile_service.domain

import com.work.profile_service.data.model.ProfileDOB
import com.work.profile_service.data.model.ProfileData
import com.work.profile_service.data.model.ProfileLocation
import com.work.profile_service.data.model.ProfileName
import com.work.profile_service.data.model.ProfilePicture
import com.work.profile_service.data.model.response.ProfileResponse
import com.work.profile_service.data.service.repository.IProfileRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProfileUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockProfileRepository: IProfileRepository

    @MockK
    private lateinit var mockDateFormatUseCase: DateFormatUseCase

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getProfileUseCase: GetProfileUseCase

    private val mockProfileData = ProfileData(
        name = ProfileName(
            title = "mr",
            first = "John",
            last = "Doe"
        ),
        dob = ProfileDOB(date = "", age = 30),
        email = "john.doe@example.com",
        gender = "male",
        picture = ProfilePicture("", "", ""),
        cell = "123-456-7890",
        location = ProfileLocation("", ProfileLocation.LocationStreet(0, ""), "", "", ""),
        nat = "US"
    )

    @Before
    fun setUp() {
        getProfileUseCase = GetProfileUseCase(
            profileRepository = mockProfileRepository,
            dateFormatter = mockDateFormatUseCase,
            coroutineDispatcher = testDispatcher
        )
    }

    @Test
    fun `invoke should return ProfileData with formatted date when repository returns valid data`() = runTest(testDispatcher) {
        val originalDate = "1993-07-20T09:44:03.411Z"
        val formattedDate = "20/07/1993"
        val profileResult = ProfileResponse(
            results = listOf(
                mockProfileData.copy(
                    dob = ProfileDOB(date = originalDate, age = 30)
                )
            )
        )
        val expectedProfileData = profileResult.results[0].copy(
            dob = ProfileDOB(date = formattedDate, age = 30)
        )

        coEvery { mockProfileRepository.getProfile() } returns flowOf(profileResult)
        every { mockDateFormatUseCase.convert(originalDate) } returns formattedDate

        val result = getProfileUseCase.invoke().first()

        Assert.assertEquals(expectedProfileData, result)
        verify(exactly = 1) { mockDateFormatUseCase.convert(originalDate) }
    }


    @Test
    fun `invoke should use hyphen for date when dateFormatter returns null`() = runTest(testDispatcher) {
        val originalDate = "1993-07-20T09:44:03.411Z"
        val profileResult = ProfileResponse(
            results = listOf(
                mockProfileData.copy(
                    dob = ProfileDOB(date = originalDate, age = 25)
                )
            ),
        )
        val expectedProfileData = profileResult.results[0].copy(
            dob = ProfileDOB(date = "-", age = 25)
        )

        coEvery { mockProfileRepository.getProfile() } returns flowOf(profileResult)
        every { mockDateFormatUseCase.convert(originalDate) } returns null

        val result = getProfileUseCase.invoke().first()

        Assert.assertEquals(expectedProfileData, result)
        verify(exactly = 1) { mockDateFormatUseCase.convert(originalDate) }
    }

    @Test
    fun `invoke should throw exception when repository returns empty results`() = runTest(testDispatcher) {
        val emptyProfileResult = ProfileResponse(results = emptyList())

        coEvery { mockProfileRepository.getProfile() } returns flowOf(emptyProfileResult)

        val exception = assertThrows(Exception::class.java) {
            runTest(testDispatcher) { // Need to re-scope for collecting flow in assertThrows
                getProfileUseCase.invoke().first()
            }
        }

        Assert.assertEquals("Profile is empty", exception.message)
        verify(exactly = 0) { mockDateFormatUseCase.convert(any()) }
    }
}