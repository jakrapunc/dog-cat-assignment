package com.work.dog_service.data.model
import com.work.dog_service.data.model.response.DogResponse

data class DogData(
    val dogResponse: DogResponse?,
    val timeStamp: String
)