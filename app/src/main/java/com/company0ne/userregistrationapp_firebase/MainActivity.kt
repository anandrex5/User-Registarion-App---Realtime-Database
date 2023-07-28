package com.company0ne.userregistrationapp_firebase

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company0ne.userregistrationapp_firebase.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    val database : FirebaseDatabase= FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    val userList= ArrayList<Users>()
    lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        //add title to the actionBar
        supportActionBar?.title = "Add User"

        mainBinding.floatingActionButton.setOnClickListener{

            val intent = Intent(this,AddUserActivity::class.java)
            startActivity(intent)
        }
        //Add swipe functionality to the recyclerView

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               //performing delete operation so make a function- getUserId on UsersAdapter

               val id =  usersAdapter.getUserId(viewHolder.adapterPosition)
                //user removeValue fun to remove the value from the recyclerView
                myReference.child(id).removeValue()
                Toast.makeText(applicationContext,"The user was deleted",Toast.LENGTH_SHORT).show()

            }
            //attach touch to the recycler
        }).attachToRecyclerView(mainBinding.recyclerView)


        retrieveDataFromDatabase()
    }
    //creating retrieveDataFromDatabase for retrieving Data from the Firebase realtime-data
    fun retrieveDataFromDatabase(){

        //ChildEventListener

        myReference.addValueEventListener(object :ValueEventListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the ArrayList prevent the data from being printed more than once.
                userList.clear()

                for(eachUser in snapshot.children){
                  val user = eachUser.getValue(Users::class.java)

                    if (user != null){
                        println("userId : ${user.userId}")
                        println("userName : ${user.userName}")
                        println("userAge : ${user.userAge}")
                        println("userEmail : ${user.userEmail}")
                        println("*****************************")

                        //all the data in the DataBase will be transferred  to the array list as an
                        //object of the Users Class
                        userList.add(user)
                    }
                    usersAdapter = UsersAdapter(this@MainActivity,userList)
                    //displaying the data
                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    mainBinding.recyclerView.adapter = usersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    //Performing delete Action from here -
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_delete_all,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.deleteAll){
            showDialogMessage()

        }
        return super.onOptionsItemSelected(item)
    }
    //show dialogMessage
    fun showDialogMessage(){
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Users")
        dialogMessage.setMessage("If you click Yes all users will be deleted."
                + " If you want to delete any specific user, you can swipe the item left or right")

        dialogMessage.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i ->
            dialogInterface.cancel()
        })
        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog: DialogInterface, i ->
             myReference.removeValue().addOnCompleteListener {  task ->
                 if(task.isSuccessful) {
                 usersAdapter.notifyDataSetChanged()
                     Toast.makeText(applicationContext,"All users were deleted",Toast.LENGTH_SHORT).show()
                 }
                 }
        })
            dialogMessage.create().show()
    }
}