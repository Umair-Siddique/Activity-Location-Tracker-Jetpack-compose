package com.example.activitylocationrecognization

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.activitylocationrecognization.Geofence.GeofenceMethods
import com.example.activitylocationrecognization.Screens.ActivityDetailScreen
import com.example.activitylocationrecognization.Screens.DetectedActivityScreen
import com.example.activitylocationrecognization.ui.theme.ActivityLocationRecognizationTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            NavHost(navController, startDestination = "detectedActivityScreen") {
                composable(route = "signUp") {
                    AuthLayout.AuthSignUpLayout(navController = navController)
                }
                composable(route = "phoneNumber") {
               UserActivityLayout.PhoneNumber(navController)
                }
                composable(route = "signIn") {
                    AuthLayout.AuthSignInLayout(navController)
                }
                composable(route = "detectedActivityScreen") {
                    DetectedActivityScreen.ActivityScreenLayout(navController = navController)
                }
                composable("activityDetail/{date}",
                    arguments = listOf(
                        navArgument("date") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    ActivityDetailScreen.ActivityDetail(
                        navController=navController,date = backStackEntry.arguments?.getString("date") ?: ""
                    )
                }
            }
            }
        }
    }