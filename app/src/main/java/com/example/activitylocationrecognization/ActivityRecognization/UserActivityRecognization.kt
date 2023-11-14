package com.example.activitylocationrecognization.ActivityRecognization


import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity


class UserActivityRecognization {

  private lateinit  var context:Context


}
fun permissionGranted(context:Context):Boolean{
   val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    if (runningQOrLater){
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        );
    } else {
        return true;
    }
}

