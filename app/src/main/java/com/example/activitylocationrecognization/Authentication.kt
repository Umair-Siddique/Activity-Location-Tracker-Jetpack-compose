package com.example.activitylocationrecognization

import android.content.Context
import android.widget.Toast
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Authentication {

    companion object{
fun createUser(context: Context,email:String, password:String){
    val  auth= Firebase.auth
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->
        if(task.isSuccessful){
            Toast.makeText(context,"User created successfully",Toast.LENGTH_SHORT).show()
    }
        else{
            Toast.makeText(context,"Error while creating user",Toast.LENGTH_SHORT).show()
        }
        }
}

fun signInUser(context: Context,email:String, password:String){
 val auth=Firebase.auth
 auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
     if(it.isSuccessful){
         Toast.makeText(context,"User log-in successfully",Toast.LENGTH_SHORT).show()
     }
     else{
         Toast.makeText(context,"Error while signing in user",Toast.LENGTH_SHORT).show()
     }
 }
}
    }

}