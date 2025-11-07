package com.kaaneneskpc.f1setupinstructor.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistory: GetHistory
) : ViewModel() {

    private val _historyItems = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyItems: StateFlow<List<HistoryItem>> = _historyItems

    init {
        viewModelScope.launch {
            getHistory().collectLatest {
                _historyItems.value = it
            }
        }
    }
}
