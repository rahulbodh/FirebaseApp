package com.rahul.firebaseapp1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore
import com.rahul.firebaseapp1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    val db = Firebase.firestore
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = Firebase.database.reference

        // Write data to the firebase realtime db
        database.child("userName").setValue("Rahul")

        val postEventListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.value
                binding.userNameValue.text = "Hello $userName"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.child("userName").addValueEventListener(postEventListener)


        // write the custom object to the firebase db
        val user = User("Rahul bodh", "123456")
        database.child("user").setValue(user)

        // read the custom object
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                binding.userNameValue.text = " ${user}"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.child("user").addValueEventListener(userEventListener)


        // write the collections to the firestore

        val user_collections = db.collection("users")

        val user1  = hashMapOf(
            "first" to "Rahul",
            "last"  to "bodh",
            "born" to "23-06-2003",
        )

        val user2 = hashMapOf(
            "first" to "Aashi",
            "last" to "Saini",
            "born" to "23-10-2003",
            )

        user_collections.document("user1").set(user1)
        user_collections.document("user2").set(user2)


        // read the collections from the firestore
        val docRef = db.collection("users").document("user1")

        docRef.get().addOnSuccessListener{ document ->
            if(document != null){
                binding.userNameValue.text = "${document.data?.get("first")}"
            }
        }

        // getting all documents from the collection
        var  documentsData : String = ""
        db.collection("users").get().addOnSuccessListener { results ->
            for (document in results){
//                binding.userNameValue.text = "${document.data}"
                documentsData += "${document.data}\n"
            }

            binding.userNameValue.text = documentsData

        }

    }
}