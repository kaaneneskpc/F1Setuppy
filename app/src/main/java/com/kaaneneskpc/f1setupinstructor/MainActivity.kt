package com.kaaneneskpc.f1setupinstructor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kaaneneskpc.f1setupinstructor.navigation.AppNavigation
import com.kaaneneskpc.f1setupinstructor.ui.theme.F1SetupInstructorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            F1SetupInstructorTheme {
                AppNavigation()
            }
        }
    }
}
