package com.work.profile.screen

import com.work.profile_service.data.model.ProfileDOB
import com.work.profile_service.data.model.ProfileData
import com.work.profile_service.data.model.ProfileLocation
import com.work.profile_service.data.model.ProfileName
import com.work.profile_service.data.model.ProfilePicture
import com.work.profile_service.domain.GetProfileUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockGetProfileUseCase: GetProfileUseCase

    private lateinit var viewModel: ProfileScreenViewModel

    private val testDispatcher = StandardTestDispatcher()

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
        Dispatchers.setMain(testDispatcher)

        coEvery { mockGetProfileUseCase.invoke() } returns flowOf(mockProfileData)

        viewModel = ProfileScreenViewModel(
            getProfileUseCase = mockGetProfileUseCase,
            coroutineDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch profile and update uiState to success`() = runTest(testDispatcher) {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        Assert.assertEquals(mockProfileData, uiState.profile)
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNull(uiState.error)

        collectJob.cancel()
    }

    @Test
    fun `init should fetch profile and update uiState to error when use case fails`() = runTest(testDispatcher) {
        val errorMessage = "Network error"
        coEvery { mockGetProfileUseCase.invoke() } returns flow { throw RuntimeException(errorMessage) }

        viewModel = ProfileScreenViewModel(mockGetProfileUseCase, testDispatcher)

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        Assert.assertNull(uiState.profile)
        Assert.assertFalse(uiState.isLoading)
        Assert.assertEquals(errorMessage, uiState.error)

        collectJob.cancel()
    }

    @Test
    fun `onUIEvent Reload should call fetchProfile`() = runTest(testDispatcher) {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        viewModel.onUIEvent(ProfileScreenViewModel.UIEvent.Reload)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        Assert.assertEquals(mockProfileData, uiState.profile)
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNull(uiState.error)

        collectJob.cancel()
    }
}