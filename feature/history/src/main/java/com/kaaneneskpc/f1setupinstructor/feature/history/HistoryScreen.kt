package com.kaaneneskpc.f1setupinstructor.feature.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val historyItems by viewModel.historyItems.collectAsState()

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(historyItems) { historyItem ->
                HistoryItemCard(historyItem = historyItem)
            }
        }
    }
}

@Composable
fun HistoryItemCard(historyItem: HistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Circuit: ${historyItem.circuit}")
            Text(text = "Weather: Quali=${historyItem.weatherQuali}, Race=${historyItem.weatherRace}")
        }
    }
}
