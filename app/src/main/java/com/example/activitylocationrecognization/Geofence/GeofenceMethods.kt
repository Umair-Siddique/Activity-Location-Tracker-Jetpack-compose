package com.example.activitylocationrecognization.Geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat
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
            geofencingClient = LocationServices.getGeofencingClient(context)

            geofenceList.add(Geofence.Builder()
                .setRequestId("req-id")
                // Set the circular region of this geofence.
                .setCircularRegion(
                    31.522166,
                    74.261949,
                    208.25F
                )
                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(100000)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
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
                    val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted }

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