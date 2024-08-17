package it.lam.pptproject

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.lam.pptproject.ui.navigation.BottomNavItem
import it.lam.pptproject.ui.navigation.NavGraph
import it.lam.pptproject.ui.theme.PPTProjectTheme

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.fitness.LocalRecordingClient

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val activityRecognitionPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Log.w("MainActivity", "Activity Recognition permission denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Perform a Play Services version check
        val hasMinPlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this, LocalRecordingClient.LOCAL_RECORDING_CLIENT_MIN_VERSION_CODE)

        if(hasMinPlayServices != ConnectionResult.SUCCESS) {

            Log.w("MainActivity", "Google Play Services troppo vecchia!")
        }

        activityRecognitionPermissionRequest.launch(android.Manifest.permission.ACTIVITY_RECOGNITION)


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
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Home", "home", ImageVector.vectorResource(R.drawable.home_24px)),
        BottomNavItem("Search", "search", ImageVector.vectorResource(R.drawable.home_24px)),
        BottomNavItem("Notifications", "notifications", ImageVector.vectorResource(R.drawable.home_24px)),
        BottomNavItem("Profile", "profile", ImageVector.vectorResource(R.drawable.account_circle_24px)),
        )


    Scaffold(
        bottomBar = { BottomNavigationBar(navController, items) }
    ) { innerPadding ->
        NavGraph(navController = navController, contentPadding = innerPadding)
    }
}



@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomNavItem>) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                label = { Text(item.name) },
                selected = currentRoute == item.route,
                onClick = {
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
