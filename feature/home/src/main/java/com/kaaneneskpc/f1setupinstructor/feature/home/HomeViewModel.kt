package com.kaaneneskpc.f1setupinstructor.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetSetups
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSetups: GetSetups
) : ViewModel() {

    private val _setups = MutableStateFlow<PagingData<Setup>>(PagingData.empty())
    val setups: StateFlow<PagingData<Setup>> = _setups

    fun findSetups(circuit: String, qualiWeather: String, raceWeather: String) {
        viewModelScope.launch {
            getSetups(circuit, qualiWeather, raceWeather, null).collectLatest {
                _setups.value = it
            }
        }
    }
}
