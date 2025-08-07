package com.work.dog_service.repository

import com.work.dog_service.domain.ConvertTimeStampUseCase
import io.mockk.junit4.MockKRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConvertTimeStampUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var convertTimeStampUseCase: ConvertTimeStampUseCase

    @Before
    fun setUp() {
        convertTimeStampUseCase = ConvertTimeStampUseCase()
    }

    @Test
    fun `invoke should return correctly formatted date string for valid timestamp`() {
        val timestamp = 1678886400000L // Corresponds to 2023-03-15 12:00:00 PM GMT
        val sdf = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        )
        val expectedDateString = sdf.format(Date(timestamp))

        val result = convertTimeStampUseCase.invoke(timestamp)

        Assert.assertEquals(expectedDateString, result)
    }

    @Test
    fun `invoke should return correctly formatted date string for another valid timestamp`() {
        val timestamp = 0L // Corresponds to Unix epoch start: 1970-01-01 00:00:00 AM GMT
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val expectedDateString = sdf.format(Date(timestamp))

        val result = convertTimeStampUseCase.invoke(timestamp)

        Assert.assertEquals(expectedDateString, result)
    }

    @Test
    fun `invoke consistency test across multiple calls with same timestamp`() {
        val timestamp = 1609459200000L // 2021-01-01 00:00:00 AM GMT
        val expectedResult = convertTimeStampUseCase.invoke(timestamp) // Call once to get reference

        val result1 = convertTimeStampUseCase.invoke(timestamp)
        val result2 = convertTimeStampUseCase.invoke(timestamp)

        Assert.assertEquals(expectedResult, result1)
        Assert.assertEquals(expectedResult, result2)
    }
}