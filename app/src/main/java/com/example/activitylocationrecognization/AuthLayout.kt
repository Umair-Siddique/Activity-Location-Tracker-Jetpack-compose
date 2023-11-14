package com.example.activitylocationrecognization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import java.nio.file.WatchEvent

class AuthLayout {
    companion object{
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthSignUpLayout(navController: NavController){
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val name=remember { mutableStateOf("") }
    if(FirebaseAuth.getInstance().currentUser!=null){
        navController.navigate("signIn")
    }
val context= LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value =name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ), modifier = Modifier.padding(16.dp))

        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ), modifier = Modifier.padding(16.dp))

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ), modifier = Modifier.padding(16.dp)
        )
        Button(onClick = {
            Authentication.createUser(context,email.value,password.value)
            navController.navigate("phoneNumber")
        }) {
            Text(text = "Create User")
        }
    }
}


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun AuthSignInLayout(navController:NavController){
            val email = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }
            val context= LocalContext.current

            Column(modifier = Modifier.fillMaxSize()) {
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ), modifier = Modifier.padding(16.dp))

                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ), modifier = Modifier.padding(16.dp)
                )
                Button(onClick = {
                    Authentication.signInUser(context,email.value,password.value)
navController.navigate("phoneNumber")
                }) {
                    Text(text = "SignIn User")
                }
            }
        }
    }
}