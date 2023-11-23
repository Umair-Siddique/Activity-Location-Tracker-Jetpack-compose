package com.example.activitylocationrecognization.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.activitylocationrecognization.Geofence.GeofenceMethods
import com.example.activitylocationrecognization.Utils

class DetectedActivityScreen {
    companion object{

        @RequiresApi(Build.VERSION_CODES.Q)
        @Composable
        fun ActivityScreenLayout(navController: NavController){

            val context= LocalContext.current
            Utils.ActivityRecognization(context)
            GeofenceMethods.GetLocation(context)
            val list= remember { mutableStateListOf<String>() }

            LaunchedEffect(true) {
                Utils.getDates(list)
            }

            LazyColumn{
                items(list.size){item->
                    val date = remember { mutableStateOf(list[item]) }
                    Card(modifier = Modifier.padding(10.dp).fillMaxWidth().padding(10.dp).clickable {
                        navController.navigate("activityDetail/${date.value}")
                    }) {
                        Text(text = "Date: " +date.value)
                    }
                }
            }
        }
    }
}