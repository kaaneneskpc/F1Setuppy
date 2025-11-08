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
import com.kaaneneskpc.f1setupinstructor.feature.results.R


@Composable
fun SetupDetailsRoute(
    viewModel: SetupDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    SetupDetailsScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                SetupDetailsEvent.BackClicked -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupDetailsScreen(
    uiState: SetupDetailsUiState,
    onEvent: (SetupDetailsEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SetupDetailsTopBar(
                onBackClick = { onEvent(SetupDetailsEvent.BackClicked) },
                onShareClick = { onEvent(SetupDetailsEvent.ShareClicked) },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            FavoriteButton(
                isFavorite = uiState.isFavorite,
                onClick = { onEvent(SetupDetailsEvent.ToggleFavorite) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
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
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        if (uiState.selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTabIndex]),
                                color = MaterialTheme.colorScheme.error,
                                height = 3.dp
                            )
                        }
                    }
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
                            selectedContentColor = MaterialTheme.colorScheme.error,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Tab Content
            item {
                Spacer(modifier = Modifier.height(16.dp))
                when (uiState.selectedTabIndex) {
                    0 -> AerodynamicsTab(
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupDetailsTopBar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(
                text = "Setup Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.semantics {
                    contentDescription = "Navigate back"
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.semantics {
                    contentDescription = "Share setup"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        scrollBehavior = scrollBehavior
    )
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
        TrackDetailsCard(details = trackDetails)
        
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
                fontWeight = FontWeight.Bold
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Visual slider (disabled/non-interactive)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .background(MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
fun TrackDetailsCard(details: TrackDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Track Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
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
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
                fontWeight = FontWeight.Bold
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
                    fontWeight = FontWeight.Bold
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
                    fontWeight = FontWeight.Bold
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
                    fontWeight = FontWeight.Bold
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
                    fontWeight = FontWeight.Bold
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
                    fontWeight = FontWeight.Bold
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics {
                    contentDescription = "$label, $value $unit"
                }
            )
            Text(
                text = "$value$unit",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Visual slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .background(MaterialTheme.colorScheme.error)
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.semantics {
                contentDescription = "$label, $value $unit"
            }
        )
        Text(
            text = String.format("%.2f%s", value, unit),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.error
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isFavorite) "Saved" else "Add to Favorites",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0E0E0E,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun SetupDetailsScreenPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFE10600),
            error = Color(0xFFE10600),
            background = Color(0xFF0E0E0E),
            surface = Color(0xFF1A1A1A),
            surfaceVariant = Color(0xFF2A2A2A),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color(0xFFB0B0B0)
        )
    ) {
        SetupDetailsScreen(
            uiState = SetupDetailsUiState(
                imageUrl = "",
                isFavorite = false
            ),
            onEvent = {}
        )
    }
}
