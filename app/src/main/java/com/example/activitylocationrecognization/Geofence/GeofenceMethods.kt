package com.example.activitylocationrecognization.Geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.example.activitylocationrecognization.Model.MyModel
import com.example.activitylocationrecognization.Utils
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceMethods {
    companion object{
        private lateinit var geofencingClient: GeofencingClient
        private val geofenceList = ArrayList<Geofence>()

        @Composable
        fun GetLocation(context: Context){
val address1= remember{ mutableStateOf("") }
            Utils.getUserData {userData->
                address1.value=userData.first
//                Toast.makeText(context,userData.first.toString(),Toast.LENGTH_LONG).show()
            }

            val address=Utils.getLatLngFromAddress(context,address1.value)
            geofencingClient = LocationServices.getGeofencingClient(context)
//Toast.makeText(context,address1.value,Toast.LENGTH_LONG).show()

            geofenceList.add(Geofence.Builder()
                .setRequestId("req-id")
                // Set the circular region of this geofence.
                .setCircularRegion(
                    address.first,
                    address.second,
                    208.25F)

                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(100000)
                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER)
                // Create the geofence.
                .build())

            val intent = Intent(context, GeofenceBroadcastReceiver::class.java)

            val pendingIntent=PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

            val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    val permissionsGranted = permissions.values.all { isPermissionGranted ->
                        isPermissionGranted }

                    if (permissionsGranted) {
                        geofencingClient.addGeofences(getGeofencingRequest(), pendingIntent).run {
                            addOnSuccessListener {
                                Toast.makeText(context,"Successfully granted permission for location",Toast.LENGTH_LONG).show()
                            }
                            addOnFailureListener {
                                Toast.makeText(context,it.message.toString(),Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                })
            SideEffect {
                locationPermissionLauncher.launch(locationPermissions)
            }
        }

        private fun getGeofencingRequest(): GeofencingRequest {
            return GeofencingRequest.Builder().apply {
                setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                addGeofences(geofenceList)
            }.build()
        }
    }
}