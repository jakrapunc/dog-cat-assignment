package com.work.profile_service.domain

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.text.format

class DateFormatUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var dateFormatUseCase: DateFormatUseCase

    @Before
    fun setUp() {
        dateFormatUseCase = DateFormatUseCase()
    }

    @Test
    fun `convert should return formatted date when DateFormatter returns valid date`() {
        val inputDate = "1993-07-20T09:44:03.411Z"
        val expectedFormattedDate = "20/07/1993"

        val result = dateFormatUseCase.convert(inputDate)

        Assert.assertEquals(expectedFormattedDate, result)
    }

    @Test
    fun `convert should return null when DateFormatter returns null`() {
        val inputDate = "invalid_date_string"


        val result = dateFormatUseCase.convert(inputDate)

        Assert.assertNull(result)
    }

    @Test
    fun `convert should handle empty input string if DateFormatter is designed for it`() {
        val inputDate = ""
        val expectedOutputForEmpty = null

        val result = dateFormatUseCase.convert(inputDate)

        Assert.assertEquals(expectedOutputForEmpty, result)
    }
}