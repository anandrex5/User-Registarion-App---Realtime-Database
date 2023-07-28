package com.company0ne.userregistrationapp_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.company0ne.userregistrationapp_firebase.databinding.ActivityUpdateUserMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateUserMainActivity : AppCompatActivity() {

    lateinit var updateUserMainBinding: ActivityUpdateUserMainBinding

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference:DatabaseReference= database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateUserMainBinding = ActivityUpdateUserMainBinding.inflate(layoutInflater)
        val view = updateUserMainBinding.root
        setContentView(view)

        //add title to the ActionBar
        supportActionBar?.title="Update User"
        getAndSetData()

        updateUserMainBinding.buttonUpdateUser.setOnClickListener {

            updateData()

        }

    }

    fun getAndSetData(){
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age",0).toString()//bcz it is int
        val email = intent.getStringExtra("email")

        updateUserMainBinding.editTextUpdateName.setText(name)
        updateUserMainBinding.editTextUpdateAge.setText(age)
        updateUserMainBinding.editTextUpdateEmail.setText(email)



    }
    fun updateData(){
        val updatedName = updateUserMainBinding.editTextUpdateName.text.toString()
        val updatedAge = updateUserMainBinding.editTextUpdateAge.text.toString().toInt()//bcz it is int
        val updatedEmail = updateUserMainBinding.editTextUpdateEmail.text.toString()

        val userId = intent.getStringExtra("id").toString()

        //crate an variable and send data in key values in map class
        val userMap= mutableMapOf<String,Any>()
        //pass keys from the model class
        userMap["userId"] = userId
        userMap["userName"] = updatedName
        userMap["userAge"] = updatedAge
        userMap["userEmail"] = updatedEmail

        myReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"The user has been updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}