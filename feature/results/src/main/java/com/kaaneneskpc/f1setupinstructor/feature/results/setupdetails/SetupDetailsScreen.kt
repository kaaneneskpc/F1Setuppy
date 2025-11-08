package com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import com.kaaneneskpc.f1setupinstructor.feature.results.R
import android.content.Intent


@Composable
fun SetupDetailsRoute(
    viewModel: SetupDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    
    SetupDetailsScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                SetupDetailsEvent.BackClicked -> onNavigateBack()
                SetupDetailsEvent.ShareClicked -> {
                    // Create share intent
                    val shareText = buildString {
                        append("🏎️ F1 Setup: ${uiState.title}\n\n")
                        append("${uiState.subtitle}\n\n")
                        append("🔧 Aerodynamics:\n")
                        append("Front Wing: ${uiState.aerodynamics.frontWingAero}\n")
                        append("Rear Wing: ${uiState.aerodynamics.rearWingAero}\n\n")
                        append("💡 ${uiState.keyPointers}\n\n")
                        append("Shared from F1 Setup Instructor")
                    }
                    
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "F1 Setup: ${uiState.title}")
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    
                    context.startActivity(
                        Intent.createChooser(intent, "Setup'ı Paylaş")
                    )
                    
                    viewModel.onEvent(event)
                }
                else -> viewModel.onEvent(event)
            }
        }
    )
}


@Composable
fun SetupDetailsScreen(
    uiState: SetupDetailsUiState,
    onEvent: (SetupDetailsEvent) -> Unit
) {
    val listState = rememberLazyListState()
    
    GradientBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Top Bar (scrolls with content)
                item {
                    SetupDetailsTopBar(
                        onBackClick = { onEvent(SetupDetailsEvent.BackClicked) },
                        onShareClick = { onEvent(SetupDetailsEvent.ShareClicked) }
                    )
                }
                
                // Header Card
                item {
                    HeaderCard(
                        imageUrl = uiState.imageUrl,
                        badge = uiState.badge,
                        title = uiState.title,
                        subtitle = uiState.subtitle,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                // Tab Bar
                item {
                    ScrollableTabRow(
                        selectedTabIndex = uiState.selectedTabIndex,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            if (uiState.selectedTabIndex < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTabIndex]),
                                    color = Color.Red,
                                    height = 3.dp
                                )
                            }
                        },
                        divider = {}
                    ) {
                        uiState.tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = index == uiState.selectedTabIndex,
                                onClick = { onEvent(SetupDetailsEvent.TabSelected(index)) },
                                text = {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = if (index == uiState.selectedTabIndex) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        }
                                    )
                                },
                                selectedContentColor = Color.Red,
                                unselectedContentColor = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

            // Tab Content
            item {
                Spacer(modifier = Modifier.height(16.dp))
                when (uiState.selectedTabIndex) {
                    0 -> AerodynamicsTab(
                        trackName = uiState.title.split(" - ").firstOrNull() ?: "Track",
                        data = uiState.aerodynamics,
                        trackDetails = uiState.trackDetails,
                        tyreStrategy = uiState.tyreStrategy,
                        keyPointers = uiState.keyPointers,
                        creatorNotes = uiState.creatorNotes,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    1 -> TransmissionTab(
                        data = uiState.transmission,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    2 -> SuspensionTab(
                        geometry = uiState.suspensionGeometry,
                        suspension = uiState.suspension,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    3 -> BrakesTab(
                        data = uiState.brakes,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    4 -> TyresTab(
                        data = uiState.tyres,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    }
                }
            }
            
            // Bottom Button (Floating - stays visible)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                FavoriteButton(
                    isFavorite = uiState.isFavorite,
                    onClick = { onEvent(SetupDetailsEvent.ToggleFavorite) }
                )
            }
        }
    }
}

@Composable
fun SetupDetailsTopBar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.semantics {
                    contentDescription = "Navigate back"
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            // Title
            Text(
                text = "Setup Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Share Button
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.semantics {
                    contentDescription = "Share setup"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun HeaderCard(
        imageUrl: String,
        badge: String,
        title: String,
        subtitle: String,
        modifier: Modifier = Modifier
    ) {
    Card(
            modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                // Image with gradient overlay
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl.ifEmpty { R.drawable.ferrari_placeholder })
                        .crossfade(true)
                        .build(),
                    contentDescription = "Setup image",
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    ),
                                    startY = size.height * 0.5f
                                )
                            )
                        },
                    contentScale = ContentScale.Crop
                )

                // Content overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.error
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Title and subtitle
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }

@Composable
fun AerodynamicsTab(
    trackName: String,
    data: AeroData,
    trackDetails: TrackDetails,
    tyreStrategy: String,
    keyPointers: String,
    creatorNotes: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Wings Card
        WingsCard(data = data)
        
        // Track Details Card
        TrackDetailsCard(
            trackName = trackName,
            details = trackDetails
        )
        
        // Tyre Strategy Card
        TextSectionCard(
            title = "Tyre Strategy",
            content = tyreStrategy
        )
        
        // Key Pointers Card
        TextSectionCard(
            title = "Key Pointers",
            content = keyPointers
        )
        
        // Creator Notes Card
        TextSectionCard(
            title = "Creator Notes",
            content = creatorNotes
        )
    }
}

@Composable
fun WingsCard(data: AeroData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Wings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                SetupValueRow(
                    label = "Front Wing Aero",
                    value = data.frontWingAero,
                    progress = data.frontWingAero / 50f
                )

                SetupValueRow(
                    label = "Rear Wing Aero",
                    value = data.rearWingAero,
                    progress = data.rearWingAero / 50f
                )
            }
        }
    }

@Composable
fun SetupValueRow(
        label: String,
        value: Int,
        progress: Float,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Visual slider (disabled/non-interactive)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .background(Color.Red)
                )
            }
        }
    }

@Composable
fun TrackDetailsCard(
        trackName: String = "Monza",
        details: TrackDetails
    ) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "$trackName Track Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // 2x2 Grid
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TrackStatCard(
                            label = "Length",
                            value = details.length,
                            modifier = Modifier.weight(1f)
                        )
                        TrackStatCard(
                            label = "Corners",
                            value = details.corners,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TrackStatCard(
                            label = "DRS Zones",
                            value = details.drsZones,
                            modifier = Modifier.weight(1f)
                        )
                        TrackStatCard(
                            label = "Ideal Laps",
                            value = details.idealLaps,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

@Composable
fun TrackStatCard(
        label: String,
        value: String,
        modifier: Modifier = Modifier
    ) {
    Card(
            modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray.copy(alpha = 0.2f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

@Composable
fun TextSectionCard(
        title: String,
        content: String,
        modifier: Modifier = Modifier
    ) {
    Card(
            modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
        }
    }

@Composable
fun TransmissionTab(
        data: TransmissionData,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Differential",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    SetupValueRowWithUnit(
                        label = "On Throttle",
                        value = data.onThrottle,
                        unit = "%",
                        progress = data.onThrottle / 100f
                    )

                    SetupValueRowWithUnit(
                        label = "Off Throttle",
                        value = data.offThrottle,
                        unit = "%",
                        progress = data.offThrottle / 100f
                    )

                    SetupValueRowWithUnit(
                        label = "Engine Braking",
                        value = data.engineBraking,
                        unit = "%",
                        progress = data.engineBraking / 100f
                    )
                }
            }
        }
    }

@Composable
fun SuspensionTab(
        geometry: SuspensionGeometryData,
        suspension: SuspensionData,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Geometry Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Geometry",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    SetupValueRowWithDecimal(
                        label = "Front Camber",
                        value = geometry.frontCamber,
                        unit = "°"
                    )

                    SetupValueRowWithDecimal(
                        label = "Rear Camber",
                        value = geometry.rearCamber,
                        unit = "°"
                    )

                    SetupValueRowWithDecimal(
                        label = "Front Toe",
                        value = geometry.frontToe,
                        unit = "°"
                    )

                    SetupValueRowWithDecimal(
                        label = "Rear Toe",
                        value = geometry.rearToe,
                        unit = "°"
                    )
                }
            }

            // Suspension Settings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Suspension",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    SimpleValueRow(label = "Front Suspension", value = suspension.frontSusp)
                    SimpleValueRow(label = "Rear Suspension", value = suspension.rearSusp)
                    SimpleValueRow(label = "Front Anti-Roll Bar", value = suspension.frontARB)
                    SimpleValueRow(label = "Rear Anti-Roll Bar", value = suspension.rearARB)
                    SimpleValueRow(label = "Front Ride Height", value = suspension.frontRideHeight)
                    SimpleValueRow(label = "Rear Ride Height", value = suspension.rearRideHeight)
                }
            }
        }
    }

@Composable
fun BrakesTab(
        data: BrakesData,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Brakes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    SetupValueRowWithUnit(
                        label = "Brake Pressure",
                        value = data.pressure,
                        unit = "%",
                        progress = data.pressure / 100f
                    )

                    SetupValueRowWithUnit(
                        label = "Front Brake Bias",
                        value = data.bias,
                        unit = "%",
                        progress = data.bias / 100f
                    )
                }
            }
        }
    }

@Composable
fun TyresTab(
        data: TyresData,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Tyre Pressures",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    SetupValueRowWithDecimal(
                        label = "Front Left",
                        value = data.frontLeftPsi,
                        unit = "PSI"
                    )

                    SetupValueRowWithDecimal(
                        label = "Front Right",
                        value = data.frontRightPsi,
                        unit = "PSI"
                    )

                    SetupValueRowWithDecimal(
                        label = "Rear Left",
                        value = data.rearLeftPsi,
                        unit = "PSI"
                    )

                    SetupValueRowWithDecimal(
                        label = "Rear Right",
                        value = data.rearRightPsi,
                        unit = "PSI"
                    )
                }
            }
        }
    }

@Composable
fun SetupValueRowWithUnit(
        label: String,
        value: Int,
        unit: String,
        progress: Float,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.semantics {
                        contentDescription = "$label, $value $unit"
                    }
                )
                Text(
                    text = "$value$unit",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Visual slider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .background(Color.Red)
                )
            }
        }
    }

@Composable
fun SetupValueRowWithDecimal(
        label: String,
        value: Double,
        unit: String,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.semantics {
                    contentDescription = "$label, $value $unit"
                }
            )
            Text(
                text = String.format("%.2f%s", value, unit),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

@Composable
fun SimpleValueRow(
        label: String,
        value: Int,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

@Composable
fun FavoriteButton(
        isFavorite: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
        elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isFavorite) "Saved" else "Add to Favorites",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }


@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun SetupDetailsScreenPreview() {
    GradientBackground {
        SetupDetailsScreen(
            uiState = SetupDetailsUiState(
                imageUrl = "",
                isFavorite = false
            ),
            onEvent = {}
        )
    }
}
