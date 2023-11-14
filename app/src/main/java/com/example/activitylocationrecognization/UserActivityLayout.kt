package com.example.activitylocationrecognization

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.activitylocationrecognization.Model.MyModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar

class UserActivityLayout {
companion object{
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PhoneNumber(navController:NavController){
        val phoneNumber= remember { mutableStateOf("") }
        val address= remember { mutableStateOf("") }
        val context= LocalContext.current

        Column(modifier = Modifier.fillMaxSize()) {
            TextField(   value =phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                label = { Text("Enter Your Phone Number") })

            TextField(   value =address.value,
                onValueChange = { address.value = it },
                label = { Text("Enter Your Address here") })

            Button(onClick = {
                val myModel=MyModel(phoneNumber = phoneNumber.value,address=address.value)
                FirebaseDatabase.getInstance().getReference("User").child(Firebase.auth.uid.toString())
                    .child("Details").setValue(myModel).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(context, "Success",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
navController.navigate("detectedActivityScreen")
            }) {
                Text(text = "Save Details")
            }
        }
    }
}
}
