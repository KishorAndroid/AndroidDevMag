package com.kishordahiwadkar.androidevmag.publisher

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kishordahiwadkar.androidevmag.R
import com.kishordahiwadkar.androidevmag.models.NewsItem
import kotlinx.android.synthetic.publisher.activity_main.*
import kotlinx.android.synthetic.publisher.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

            anonymousAuthentication()

        }
    }

    private fun anonymousAuthentication() {
        val mAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = mAuth.currentUser
        if (user == null) {
            authenticateUser()
        } else {
            uploadData()
        }
    }

    private fun authenticateUser() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                uploadData()
            } else {
                Snackbar.make(fab, "Error occured : " + task.exception, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

    private fun uploadData() {
        var title: String = editTitle.text.toString()
        var description: String = editDescription.text.toString()
        var imageUri: String = editImageUri.text.toString()

        if (!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(description)) {

            var newItem = NewsItem()
            newItem.title = title
            newItem.description = description
            newItem.imageUri = imageUri
            val timestampNow = HashMap<String, Any>()
            timestampNow["timestamp"] = ServerValue.TIMESTAMP
            newItem.timestampCreated = timestampNow

            var firebaseDB = FirebaseDatabase.getInstance()
            var dbRef = firebaseDB.getReference("articles")

            dbRef.push().setValue(newItem)
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Snackbar.make(fab, "Data changed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(fab, "Error : " + error.message , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            })

        } else {
            Snackbar.make(fab, "Enter title and description", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}
