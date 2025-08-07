package com.work.dog_service.repository

import com.work.dog_service.data.model.response.DogResponse
import com.work.dog_service.data.service.repository.IDogRepository
import com.work.dog_service.domain.ConvertTimeStampUseCase
import com.work.dog_service.domain.GetSequenceDogListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class GetSequenceDogListUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockDogRepository: IDogRepository

    @MockK
    private lateinit var mockConvertTimeStampUseCase: ConvertTimeStampUseCase

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getSequenceDogListUseCase: GetSequenceDogListUseCase

    @Before
    fun setUp() {
        getSequenceDogListUseCase = GetSequenceDogListUseCase(
            dogRepository = mockDogRepository,
            convertTimeStampUseCase = mockConvertTimeStampUseCase,
            coroutineDispatcher = testDispatcher
        )
    }

    @Test
    fun `invoke should return list of DogData with formatted timestamps for successful sequential calls`() = runTest(testDispatcher) {
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

        val result = getSequenceDogListUseCase.invoke(size).first()

        Assert.assertEquals(size, result.size)
        Assert.assertEquals(dogResponse1, result[0]?.dogResponse)
        Assert.assertEquals(formattedTimestamp1, result[0]?.timeStamp)
        Assert.assertEquals(dogResponse2, result[1]?.dogResponse)
        Assert.assertEquals(formattedTimestamp2, result[1]?.timeStamp)
        Assert.assertEquals(dogResponse3, result[2]?.dogResponse)
        Assert.assertEquals(formattedTimestamp3, result[2]?.timeStamp)

        coVerify(exactly = size) { mockDogRepository.getRandomDog() }
        verify(exactly = size) { mockConvertTimeStampUseCase.invoke(allAny()) }
    }

    @Test
    fun `invoke should return list with null for DogData when a repository call fails sequentially`() = runTest(testDispatcher) {
        val size = 3
        val dogResponse1 = DogResponse("url1", "success")
        val dogResponse3 = DogResponse("url3", "success")
        val failureException = RuntimeException("API error")
        val formattedTimestamp1 = "01 Jan 2023, 10:00 AM"
        val formattedTimestamp2 = "01 Jan 2023, 10:01 AM" // Timestamp for the failed call
        val formattedTimestamp3 = "01 Jan 2023, 10:02 AM"

        coEvery { mockDogRepository.getRandomDog() } returnsMany listOf(
            flowOf(dogResponse1),
            flow { throw failureException },
            flowOf(dogResponse3)
        )
        every { mockConvertTimeStampUseCase.invoke(any()) } returnsMany listOf(
            formattedTimestamp1,
            formattedTimestamp2,
            formattedTimestamp3
        )

        val result = getSequenceDogListUseCase.invoke(size).first()

        Assert.assertEquals(size, result.size)
        Assert.assertNotNull(result[0])
        Assert.assertEquals(dogResponse1, result[0]?.dogResponse)
        Assert.assertEquals(formattedTimestamp1, result[0]?.timeStamp)

        Assert.assertNull(result[1])

        Assert.assertNotNull(result[2])
        Assert.assertEquals(dogResponse3, result[2]?.dogResponse)
        Assert.assertEquals(formattedTimestamp2, result[2]?.timeStamp)


        coVerify(exactly = size) { mockDogRepository.getRandomDog() }
        verify(exactly = 2) { mockConvertTimeStampUseCase.invoke(allAny()) }
    }

}