package com.example.activitylocationrecognization

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat
import com.example.activitylocationrecognization.ActivityRecognization.MyReceiver
import com.example.activitylocationrecognization.Model.ActivityModel
import com.example.activitylocationrecognization.Model.MyModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

class Utils {
    companion object{
        fun toActivityString(activity: Int): String {
            return when (activity) {
                DetectedActivity.STILL -> "STILL"
                DetectedActivity.WALKING -> "WALKING"
                DetectedActivity.IN_VEHICLE -> "IN VEHICLE"
                DetectedActivity.RUNNING -> "RUNNING"
                else -> "UNKNOWN"
            }
        }

        // type of transitions
        fun toTransitionType(transitionType: Int): String {
            return when (transitionType) {
                ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
                ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
                else -> "UNKNOWN"
            }
        }
         fun getDateTime(): String {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HH:mm")
            return formatter.format(time)
        }

      fun getTodayDate():String{
            val calendar= Calendar.getInstance()
            val month=calendar.get(Calendar.MONTH)+1
            val year=calendar.get(Calendar.YEAR)
            val day=calendar.get(Calendar.DAY_OF_MONTH)
            val todayDate="$day-$month-$year";
            return todayDate
        }


        @OptIn(ExperimentalPermissionsApi::class)
        @RequiresApi(Build.VERSION_CODES.Q)
        @Composable
        fun ActivityRecognization(context: Context){
            val transitions = mutableListOf<ActivityTransition>()

            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()

            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()

            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
            transitions += ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
            val request = ActivityTransitionRequest(transitions)
            val intent = Intent(context, MyReceiver::class.java)
            val pendingIntent= PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_MUTABLE);
            val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED

            val activityPermissionState = rememberPermissionState(
                Manifest.permission.ACTIVITY_RECOGNITION
            )
            if (activityPermissionState.status.isGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    ActivityRecognition.getClient(context)
                        .requestActivityTransitionUpdates(request, pendingIntent)  .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Successfully detected activity", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, it.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                            }
            }
        }
            SideEffect {
                activityPermissionState.launchPermissionRequest()
            }
        }


        fun dialPhoneNumber(context: Context,phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
                context.startActivity(intent)
        }

        fun getActivityData(context: Context,list:MutableList<ActivityModel>,date:String){
            FirebaseDatabase.getInstance().getReference("Activity").child(FirebaseAuth.getInstance().uid.toString())
                .child(date)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children){
                            val data =dataSnapshot.getValue(ActivityModel::class.java)
Log.d("val",dataSnapshot.value.toString())
                            if(data!=null){
                                list.add(ActivityModel(data.activity.toString(),data.date.toString()
                                ,data.timeStamp))
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                      Toast.makeText(context,error.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                })
        }

        fun storeDates(date: String):String{
            FirebaseDatabase.getInstance().getReference("Dates").push().setValue(date);
            return date
        }

        fun getDates(list:MutableList<String>){
            val currentDate=getTodayDate()
            FirebaseDatabase.getInstance().getReference("Dates").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataSnapshot in snapshot.children){
                        val data = dataSnapshot.getValue(String::class.java)
                            if(data != null && !list.contains(currentDate)){
                                storeDates(currentDate)
                                list.add(data.toString())
                            }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("date",error.message.toString())
                }
            })
        }

        fun getLatLngFromAddress(context: Context,address: String): Pair<Double, Double> {
            val geocoder = Geocoder(context)
            var lat=0.0
            var long=0.0
            try {
                val addresses: List<Address> = geocoder.getFromLocationName(address, 1)!!
                if (addresses.isNotEmpty()) {
                  lat = addresses[0].latitude
                    long = addresses[0].longitude
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return Pair(lat, long)
        }

        fun getUserData(callback: (Pair<String, String>) -> Unit) {
            var address = ""
            var phoneNo = ""
            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().uid.toString())
                .child("Details").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.getValue(MyModel::class.java)
                        address = data?.address.toString()
                        phoneNo = data?.phoneNumber.toString()

                        // Call the callback with the retrieved data
                        callback.invoke(Pair(address, phoneNo))
                    }

                    override fun onCancelled(error: DatabaseError) {
                       // Handle onCancelled if needed
                    }
                })
        }

    }
}