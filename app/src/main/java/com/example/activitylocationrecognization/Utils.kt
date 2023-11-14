package com.example.activitylocationrecognization

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.activitylocationrecognization.ActivityRecognization.MyReceiver
import com.example.activitylocationrecognization.Model.ActivityModel
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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


        fun activityRecognization(context: Context){
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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityRecognition.getClient(context).requestActivityTransitionUpdates(request, pendingIntent)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(context,"Successfully detected activity", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, it.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
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
                        if (data != null){
                            list.add(data.toString())
                        }
                            if(!list.contains(currentDate)){
                                storeDates(currentDate)
                            }

                    }

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        }
    }
}