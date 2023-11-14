package com.example.activitylocationrecognization.Screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.activitylocationrecognization.Model.ActivityModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityScreen {
    companion object{
@Composable
fun ActivityScreenLayout(){
    val date = remember { mutableStateOf("") }
    val list= remember { mutableStateListOf<ActivityModel>() }
    getActivityData(list)

    LazyColumn{
        items(list.size){item->
            date.value= list[item].date.toString()
            Card(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                Text(text = date.value)
            }
        }
    }

}


    private fun getActivityData(list:MutableList<ActivityModel>):MutableList<ActivityModel>{
        FirebaseDatabase.getInstance().getReference("Activity").child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   for (dataSnapshot in snapshot.children){
                    val data =snapshot.getValue(ActivityModel::class.java)
                    if(data!=null){
  list.add(data)
                    } } }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return list
    }
    }
}
