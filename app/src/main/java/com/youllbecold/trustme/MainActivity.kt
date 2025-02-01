package com.youllbecold.trustme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.youllbecold.trustme.ui.components.Toolbar
import com.youllbecold.trustme.ui.navigation.NavGraph
import com.youllbecold.trustme.ui.navigation.NavigationBar
import com.youllbecold.trustme.ui.screens.overlays.LocationPermissionScreen
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.MainViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YoullBeColdTheme { Main() }
        }
    }
}

@Composable
fun Main() {
    val navController: NavHostController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel<MainViewModel>()

    Box {
        // Setup main navigation structure
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { Toolbar() },
            bottomBar = { NavigationBar(navController) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavGraph(
                    navController,
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        val locationGranted = viewModel.locationGranted.collectAsStateWithLifecycle()

        when {
            // TODO: Welcome screen on first visit.

            !locationGranted.value -> {
                // Show overlay over everything as needed
                LocationPermissionScreen(viewModel::refreshLocationPermissionState)
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    YoullBeColdTheme {
        Main()
    }
}
