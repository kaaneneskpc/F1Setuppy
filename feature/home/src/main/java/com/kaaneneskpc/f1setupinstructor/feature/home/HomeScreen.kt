package com.kaaneneskpc.f1setupinstructor.feature.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToSetupDetails -> {
                    navController.navigate("setup_details/${event.trackName}")
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flag icon and Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = com.kaaneneskpc.f1setupinstructor.core.ui.R.drawable.ic_flag),
                        contentDescription = "Flag",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "F1 Setup Asistanı",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                // Profile icon
                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        item {
            Section(title = "Yarış Pisti") {
                OutlinedTextField(
                    value = uiState.track,
                    onValueChange = { viewModel.onEvent(HomeEvent.TrackChanged(it)) },
                    label = { Text("Pist adını girin...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Red,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        }

        item {
            Section(title = "Favori Pistler") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FavoriteTrackChip("Monza", true) {
                        viewModel.onEvent(HomeEvent.TrackChanged("Monza"))
                    }
                    FavoriteTrackChip("Silverstone", true) {
                        viewModel.onEvent(HomeEvent.TrackChanged("Silverstone"))
                    }
                    FavoriteTrackChip("Spa") {
                        viewModel.onEvent(HomeEvent.TrackChanged("Spa"))
                    }
                    FavoriteTrackChip("Suzuka") {
                        viewModel.onEvent(HomeEvent.TrackChanged("Suzuka"))
                    }
                }
            }
        }

        item {
            Section(title = "Setup Türü") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SetupTypeChip(
                        text = "Sıralama",
                        selected = uiState.setupType == "QUALIFYING",
                        onClick = { viewModel.onEvent(HomeEvent.SetupTypeChanged("QUALIFYING")) },
                        modifier = Modifier.weight(1f)
                    )
                    SetupTypeChip(
                        text = "Yarış",
                        selected = uiState.setupType == "RACE",
                        onClick = { viewModel.onEvent(HomeEvent.SetupTypeChanged("RACE")) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            Section(title = "Hava Durumu") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (uiState.setupType) {
                        "QUALIFYING" -> {
                            WeatherSelector(
                                title = "Sıralama Turu Hava Durumu",
                                selectedWeather = uiState.qualyWeather,
                                onWeatherSelected = { viewModel.onEvent(HomeEvent.QualyWeatherChanged(it)) }
                            )
                        }
                        "RACE" -> {
                            WeatherSelector(
                                title = "Yarış Hava Durumu",
                                selectedWeather = uiState.raceWeather,
                                onWeatherSelected = { viewModel.onEvent(HomeEvent.RaceWeatherChanged(it)) }
                            )
                        }
                    }
                }
            }
        }

        item {
            Section(title = "En Son Setup") {
                LastSetupCard()
            }
        }

        item {
            Button(
                onClick = { viewModel.onEvent(HomeEvent.GetSetupClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Setup Önerisi Al", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        // Error message
        if (uiState.error != null) {
            item {
                Text(
                    text = uiState.error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
    
    // Error Dialog
    if (uiState.error != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.onEvent(HomeEvent.DismissError) },
            title = {
                Text(
                    text = "Hata",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = uiState.error,
                    color = Color.White.copy(alpha = 0.9f)
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(HomeEvent.DismissError) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Tamam", color = Color.White)
                }
            },
            containerColor = Color.DarkGray.copy(alpha = 0.95f),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun WeatherSelector(
    title: String,
    selectedWeather: String,
    onWeatherSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(title, color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeatherConditionChip(
                iconRes = R.drawable.ic_sunny,
                text = "Güneşli",
                selected = selectedWeather == "Dry",
                onClick = { onWeatherSelected("Dry") }
            )
            WeatherConditionChip(
                iconRes = R.drawable.ic_cloudy,
                text = "Bulutlu",
                selected = selectedWeather == "Cloudy",
                onClick = { onWeatherSelected("Cloudy") }
            )
            WeatherConditionChip(
                iconRes = R.drawable.ic_rainy,
                text = "Yağmurlu",
                selected = selectedWeather == "Wet",
                onClick = { onWeatherSelected("Wet") }
            )
        }
    }
}


@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        content()
    }
}

@Composable
fun FavoriteTrackChip(text: String, hasIcon: Boolean = false, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (hasIcon) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(text, color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherConditionChip(
    @DrawableRes iconRes: Int,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color.DarkGray.copy(alpha = 0.5f)
    val contentColor = if (selected) Color.Red else Color.Gray
    val border = if (selected) BorderStroke(1.dp, Color.Red) else null

    Card(
        modifier = modifier.size(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = contentColor, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}


@Composable
fun LastSetupCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Monza", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("2 gün önce", color = Color.Gray, fontSize = 12.sp)
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Tekrar Kullan", color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SetupDetail(title = "Ön Kanat", value = "8")
                SetupDetail(title = "Arka Kanat", value = "9")
            }
        }
    }
}

@Composable
fun SetupDetail(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SetupTypeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) Color.Red else Color.DarkGray.copy(alpha = 0.5f)
    val contentColor = if (selected) Color.White else Color.Gray

    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GradientBackground {
        HomeScreen(navController = rememberNavController())
    }
}
