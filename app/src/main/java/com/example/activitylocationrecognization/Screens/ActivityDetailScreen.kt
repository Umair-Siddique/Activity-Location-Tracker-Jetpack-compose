package com.example.activitylocationrecognization.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.activitylocationrecognization.Model.ActivityModel
import com.example.activitylocationrecognization.Utils

class ActivityDetailScreen {
    companion object{
        @Composable
        fun ActivityDetail(navController: NavController,date:String){

            val context= LocalContext.current
            Toast.makeText(context,date.toString(),Toast.LENGTH_SHORT).show()
            val list= remember { mutableStateListOf<ActivityModel>() }

            LaunchedEffect(true ){
               Utils.getActivityData(context,list,date)
            }
            LazyColumn(){
                items(list.size){items->
                    val activity= remember { mutableStateOf(list[items].activity) }
                    val todayDate= remember { mutableStateOf(list[items].date) }
                    val timeStamp= remember { mutableStateOf(list[items].timeStamp) }
                    Card(modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .padding(10.dp)) {
                        Text(text = activity.value.toString())
                        Text(text = todayDate.value.toString())
                        Text(text = timeStamp.value.toString())
                    }
                }
            }

        }

    }


}