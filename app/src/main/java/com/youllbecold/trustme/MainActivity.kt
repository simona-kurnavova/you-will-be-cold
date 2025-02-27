package com.youllbecold.trustme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.youllbecold.trustme.ui.components.Toolbar
import com.youllbecold.trustme.ui.navigation.FloatingAction
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

    LaunchedEffect(overlayState) {
        when(overlayState) {
            OverlayState.NEW_USER -> navController.popAllAndNavigate(NavRoute.Welcome.route)
            OverlayState.LOCATION_PERM_MISSING ->
                navController.popAllAndNavigate(NavRoute.LocationPermission.route)
            else -> Unit // Do nothing
        }
    }

    Box {
        // Setup main navigation structure
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                navController.currentRoute()?.let { route ->
                    val toolbar = NavRouteItem.fromRoute(route).getToolbar()
                    toolbar?.let {
                        Toolbar(
                            title = stringResource(it.toolbarTitle),
                            iconType = it.toolbarIcon,
                            showInfoAction = it.showInfoAction,
                            navigateToInfo = { navController.navigate(NavRoute.Info.route) }
                        )
                    }
                }
            },
            bottomBar = {
                if (showBottomBar(navController.currentRoute())) {
                    NavigationBar(navController)
                }
            },
            floatingActionButton = {
                getFloatingButton(navController.currentRoute())?.let { action ->
                    SetupFloatingButton(navController, action)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavGraph(navController)
            }
        }
    }
}

@Composable
private fun showBottomBar(currentRoute: String?): Boolean {
    val route = currentRoute ?: return false
    return NavRouteItem.fromRoute(route).isMenuItem()
}

@Composable
private fun getFloatingButton(currentRoute: String?): FloatingAction? {
    val route = currentRoute ?: return null
    return NavRouteItem.fromRoute(route).getFloatingAction()
}

@Composable
private fun SetupFloatingButton(navController: NavController, action: FloatingAction) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate(action.floatingActionTo.route) },
        icon = {
            Icon(action.floatingActionIcon, null)
        },
        text = {
            Text(text = stringResource(id = action.floatingActionTitle))
         },
    )
}
