package com.work.dog.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.dog_service.data.model.DogData
import com.work.dog_service.domain.GetConcurrentDogListUseCase
import com.work.dog_service.domain.GetSequenceDogListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DogScreenViewModel(
    private val getConcurrentDogListUseCase: GetConcurrentDogListUseCase,
    private val getSequenceDogListUseCase: GetSequenceDogListUseCase,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _dogList = MutableStateFlow<List<DogData?>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _dogList,
        _isLoading,
        _error
    ) { dogList, isLoading, error ->
        UIState(
            dogList = dogList,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UIState()
    )

    init {
        loadConcurrentDogList(3)
    }

    fun loadConcurrentDogList(size: Int) {
        viewModelScope.launch {
            getConcurrentDogListUseCase.invoke(size)
                .onStart {
                    _isLoading.value = true
                }.catch {
                    _isLoading.value = false
                    _error.value = it.message
                }.flowOn(
                    coroutineDispatcher
                ).collect {
                    _dogList.value = it
                    _isLoading.value = false
                }
        }
    }

    fun loadSequenceDogList(size: Int) {
        viewModelScope.launch {
            getSequenceDogListUseCase.invoke(size)
                .onStart {
                    _isLoading.value = true
                }.catch {
                    _isLoading.value = false
                    _error.value = it.message
                }.flowOn(
                    coroutineDispatcher
                ).collect {
                    _dogList.value = it
                    _isLoading.value = false
                }
        }
    }

    fun onEvent(event: UIEvent) {
        when (event) {
            UIEvent.SequenceReload -> loadSequenceDogList(3)
            UIEvent.ConcurrentReload -> loadConcurrentDogList(3)
        }
    }

    sealed interface UIEvent {
        object SequenceReload : UIEvent
        object ConcurrentReload : UIEvent
    }

    data class UIState(
        val dogList: List<DogData?> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}