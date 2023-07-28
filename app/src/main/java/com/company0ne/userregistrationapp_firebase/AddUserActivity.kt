
package com.company0ne.userregistrationapp_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.company0ne.userregistrationapp_firebase.R
import com.company0ne.userregistrationapp_firebase.databinding.ActivityAddUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserActivity : AppCompatActivity() {

    lateinit var addUserBinding: ActivityAddUserBinding

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference= database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = addUserBinding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        addUserBinding.buttonAddUser.setOnClickListener {

            //call this addUserToDatabase
            addUserToDatabase()

        }
    }
    fun addUserToDatabase(){
       val name : String =  addUserBinding.editTextName.text.toString();
        val age : Int = addUserBinding.editTextAge.text.toString().toInt()
       val email : String= addUserBinding.editTextEmail.text.toString()

        //let make unique key
       val id :String =  myReference.push().key.toString()
        val users= Users(id,name,age,email)
        myReference.child(id).setValue(users).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, " The new user has been added to the database",Toast.LENGTH_SHORT)
                    .show()
                finish()
            }else {
            Toast.makeText(applicationContext,task.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}