package com.work.profile.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.profile_service.data.model.ProfileData
import com.work.profile_service.domain.GetProfileUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _profileData = MutableStateFlow<ProfileData?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _profileData,
        _isLoading,
        _error
    ) { profile, isLoading, error ->
        UIState(profile, isLoading, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UIState()
    )

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            getProfileUseCase.invoke()
                .catch {
                    _isLoading.value = false
                    _error.value = it.message
                }.flowOn(
                    coroutineDispatcher
                ).collect {
                    _profileData.value = it
                    _isLoading.value = false
                }
        }
    }

    fun onUIEvent(event: UIEvent) {
        when (event) {
            UIEvent.Reload -> fetchProfile()
        }
    }

    sealed class UIEvent {
        object Reload : UIEvent()
    }

    data class UIState(
        val profile: ProfileData? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}