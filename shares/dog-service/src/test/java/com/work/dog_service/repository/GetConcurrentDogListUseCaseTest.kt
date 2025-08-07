package com.work.dog_service.repository

import com.work.dog_service.data.model.response.DogResponse
import com.work.dog_service.data.service.api.DogService
import com.work.dog_service.data.service.repository.IDogRepository
import com.work.dog_service.domain.ConvertTimeStampUseCase
import com.work.dog_service.domain.GetConcurrentDogListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetConcurrentDogListUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockDogRepository: IDogRepository

    @MockK
    private lateinit var mockConvertTimeStampUseCase: ConvertTimeStampUseCase

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getConcurrentDogListUseCase: GetConcurrentDogListUseCase

    @Before
    fun setUp() {
        getConcurrentDogListUseCase = GetConcurrentDogListUseCase(
            dogRepository = mockDogRepository,
            convertTimeStampUseCase = mockConvertTimeStampUseCase,
            coroutineDispatcher = testDispatcher
        )
    }

    @Test
    fun `invoke should return list of DogData with formatted timestamps for successful calls`() = runTest(testDispatcher) {
        val size = 3
        val dogResponse1 = DogResponse("url1", "success")
        val dogResponse2 = DogResponse("url2", "success")
        val dogResponse3 = DogResponse("url3", "success")
        val formattedTimestamp1 = "01 Jan 2023, 10:00 AM"
        val formattedTimestamp2 = "01 Jan 2023, 10:01 AM"
        val formattedTimestamp3 = "01 Jan 2023, 10:02 AM"

        coEvery { mockDogRepository.getRandomDog() } returnsMany listOf(
            flowOf(dogResponse1),
            flowOf(dogResponse2),
            flowOf(dogResponse3)
        )

        every { mockConvertTimeStampUseCase.invoke(any()) } returnsMany listOf(
            formattedTimestamp1,
            formattedTimestamp2,
            formattedTimestamp3
        )

        val result = getConcurrentDogListUseCase.invoke(size).first()

        Assert.assertEquals(size, result.size)
        Assert.assertEquals(dogResponse1, result[0]?.dogResponse)
        Assert.assertEquals(formattedTimestamp1, result[0]?.timeStamp)
        Assert.assertEquals(dogResponse2, result[1]?.dogResponse)
        Assert.assertEquals(formattedTimestamp2, result[1]?.timeStamp)
        Assert.assertEquals(dogResponse3, result[2]?.dogResponse)
        Assert.assertEquals(formattedTimestamp3, result[2]?.timeStamp)

        verify(exactly = size) { mockConvertTimeStampUseCase.invoke(allAny()) }
    }

    @Test
    fun `invoke should return list with nulls for failed repository calls`() = runTest(testDispatcher) {
        val size = 3
        val dogResponse1 = DogResponse("url1", "success")
        val dogResponse2 = DogResponse("url2", "success")
        val failureException = RuntimeException("API error")
        val formattedTimestamp1 = "01 Jan 2023, 10:00 AM"
        val formattedTimestampForFailedCall = "01 Jan 2023, 10:01 AM" // Timestamp is still captured

        coEvery { mockDogRepository.getRandomDog() } returnsMany listOf(
            flowOf(dogResponse1),
            flow { throw failureException },
            flowOf(dogResponse2)
        )
        every { mockConvertTimeStampUseCase.invoke(any()) } returnsMany listOf(
            formattedTimestamp1,
            formattedTimestampForFailedCall, // For the call that will fail
            "01 Jan 2023, 10:02 AM"
        )

        val result = getConcurrentDogListUseCase.invoke(size).first()

        Assert.assertEquals(size, result.size)
        Assert.assertNotNull(result[0])
        Assert.assertEquals(dogResponse1, result[0]?.dogResponse)
        Assert.assertEquals(formattedTimestamp1, result[0]?.timeStamp)

        Assert.assertEquals(null, result[1]?.dogResponse) // DogResponse is null due to exception
        Assert.assertEquals(formattedTimestampForFailedCall, result[1]?.timeStamp) // Timestamp should still be there

        Assert.assertNotNull(result[2])
        Assert.assertEquals("url2", result[2]?.dogResponse?.message)

        verify(exactly = size) { mockConvertTimeStampUseCase.invoke(allAny()) }
    }

    @Test
    fun `invoke should return empty list for size zero`() = runTest(testDispatcher) {
        val result = getConcurrentDogListUseCase.invoke(0).first()

        Assert.assertTrue(result.isEmpty())
        verify(exactly = 0) { mockConvertTimeStampUseCase.invoke(any()) }
    }

    @Test
    fun `invoke should handle all repository calls failing`() = runTest(testDispatcher) {
        val size = 2
        val failureException = RuntimeException("API error")
        every { mockConvertTimeStampUseCase.invoke(any()) } returns "N/A"
        coEvery { mockDogRepository.getRandomDog() } returns flow { throw failureException }


        val result = getConcurrentDogListUseCase.invoke(size).first()

        Assert.assertEquals(size, result.size)
        Assert.assertTrue(result.all { it?.dogResponse == null }) // All dogResponses should be null
        Assert.assertTrue(result.all { it?.timeStamp == "N/A" }) // All timestamps should be the placeholder

        verify(exactly = size) { mockConvertTimeStampUseCase.invoke(allAny()) }
    }
}