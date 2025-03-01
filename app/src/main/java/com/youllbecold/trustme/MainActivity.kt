package com.youllbecold.trustme

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.youllbecold.trustme.ui.components.Toolbar
import com.youllbecold.trustme.ui.components.generic.ThemedText
import com.youllbecold.trustme.ui.components.generic.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.ui.navigation.NavGraph
import com.youllbecold.trustme.ui.navigation.NavRoute
import com.youllbecold.trustme.ui.navigation.NavRouteItem
import com.youllbecold.trustme.ui.navigation.NavigationBar
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.MainViewModel
import com.youllbecold.trustme.ui.viewmodels.OverlayState
import com.youllbecold.trustme.ui.utils.currentRoute
import com.youllbecold.trustme.ui.utils.popAllAndNavigate
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            YoullBeColdTheme {
                Main()
            }
        }
    }
}

@Composable
private fun Main() {
    val navController: NavHostController = rememberNavController()

    val viewModel: MainViewModel = koinViewModel<MainViewModel>()

    val overlayState by viewModel.overlayState.collectAsStateWithLifecycle()

    // Handle different overlays: welcome screen or location permission.
    LaunchedEffect(overlayState) {
        when(overlayState) {
            OverlayState.NEW_USER -> navController.popAllAndNavigate(NavRoute.Welcome.route)
            OverlayState.LOCATION_PERM_MISSING ->
                navController.popAllAndNavigate(NavRoute.LocationPermission.route)
            else -> Unit // Do nothing
        }
    }

    val route = navController.currentRoute()?.let { NavRouteItem.fromRoute(it) }

    // Setup main navigation structure
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SetupToolbar(navController, route) },
        bottomBar = { SetupBottomBar(navController, route) },
        floatingActionButton = { SetupFloatingButton(navController, route) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController)
        }
    }
}

@Composable
private fun SetupToolbar(navController: NavController, currentRoute: NavRouteItem?) {
    currentRoute?.getToolbar()?.let {
        Toolbar(
            title = stringResource(it.toolbarTitle),
            iconType = it.toolbarIcon,
            showInfoAction = it.showInfoAction,
            navigateToInfo = { navController.navigate(NavRoute.Info.route) }
        )
    }
}

@Composable
private fun SetupBottomBar(navController: NavController, currentRoute: NavRouteItem?) {
    if (currentRoute?.isMenuItem() == true) {
        NavigationBar(navController)
    }
}

@Composable
private fun SetupFloatingButton(navController: NavController,  currentRoute: NavRouteItem?) {
    currentRoute?.getFloatingAction()?.let { action ->
        ExtendedFloatingActionButton(
            onClick = { navController.navigate(action.floatingActionTo.route) },
            icon = {
                Icon(
                    action.floatingActionIcon,
                    null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            text = {
                ThemedText(
                    text = stringResource(id = action.floatingActionTitle),
                    textAttr = defaultMediumTextAttr().copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            },
        )
    }
}
