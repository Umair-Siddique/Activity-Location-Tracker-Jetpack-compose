package com.example.activitylocationrecognization.ActivityRecognization

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.activitylocationrecognization.MainActivity
import com.example.activitylocationrecognization.Model.ActivityModel
import com.example.activitylocationrecognization.Model.MyModel
import com.example.activitylocationrecognization.Utils
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)!!
            for (event in result.transitionEvents) {
val info="Detected Activity: "+Utils.toActivityString(event.activityType) +" Transition: "+Utils.toTransitionType(event.transitionType)
                val model=ActivityModel(info,Utils.getTodayDate(),Utils.getDateTime())
        FirebaseDatabase.getInstance().getReference("Activity").child(FirebaseAuth.getInstance().uid.toString())
            .child(Utils.getTodayDate())
            .push().setValue(model)

            }
        }
    }
}