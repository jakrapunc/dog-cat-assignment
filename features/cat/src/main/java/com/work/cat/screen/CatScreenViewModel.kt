package com.work.cat.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.cat_service.data.model.request.BreedsRequest
import com.work.cat_service.data.model.response.CatBreedsResponse
import com.work.cat_service.data.service.repository.ICatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatScreenViewModel(
    private val catRepository: ICatRepository,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _breeds = MutableStateFlow<CatBreedsResponse?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _breeds,
        _isLoading,
        _error
    ) { items, isLoading, error ->
        UIState(
            breeds = items,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UIState()
    )

    init {
        fetchBreeds()
    }

    fun fetchBreeds() {
        viewModelScope.launch {
            catRepository.getCatBreeds(
                BreedsRequest(
                    limit = 25
                )
            ).onStart {
                _isLoading.value = true
            }.catch {
                _isLoading.value = false
                _error.value = it.message
            }.flowOn(
                coroutineDispatcher
            ).collect { result ->
                _breeds.value = result
            }
        }
    }

    data class UIState(
        val breeds: CatBreedsResponse? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}