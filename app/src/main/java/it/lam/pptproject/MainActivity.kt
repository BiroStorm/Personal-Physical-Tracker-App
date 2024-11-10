package it.lam.pptproject

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.lam.pptproject.ui.navigation.NavGraph
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.screen.CalendarDestination
import it.lam.pptproject.ui.screen.ChartsDestination
import it.lam.pptproject.ui.screen.HomeDestination
import it.lam.pptproject.ui.screen.ProfileDestination
import it.lam.pptproject.ui.theme.PPTProjectTheme
import it.lam.pptproject.utils.PermissionManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val activityRecognitionPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            PermissionManager.forcePermissionRequest(this)
        } else {

            (application as PPTApplication).subscribeToStepCount()
        }
    }


    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            (application as PPTApplication).subscribeToStepCount()
        } else {
            activityRecognitionPermissionRequest.launch(android.Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermission()


        enableEdgeToEdge()
        setContent {
            PPTProjectTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }

    }


}

@Composable
fun InventoryNavHost(
    navController: NavHostController = rememberNavController(),
) {

    val listOfDestination = listOf(
        HomeDestination,
        ChartsDestination,
        CalendarDestination,
        ProfileDestination
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, listOfDestination) }
    ) { innerPadding ->
        NavGraph(navController = navController, contentPadding = innerPadding)
    }
}


@Composable
fun BottomNavigationBar(navController: NavController, items: List<NavigationDestination>) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.icon),
                        contentDescription = stringResource(item.name)
                    )
                },
                label = { Text(stringResource(item.name)) },
                selected = currentRoute == item.route,
                onClick = {
                    Log.i(
                        "MainActivity",
                        "Navigating to ${item.route} and currentRoute is $currentRoute"
                    )
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
