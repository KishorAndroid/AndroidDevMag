package com.kishordahiwadkar.androidevmag.publisher

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kishordahiwadkar.androidevmag.R
import com.kishordahiwadkar.androidevmag.models.NewsItem
import kotlinx.android.synthetic.publisher.activity_main.*
import kotlinx.android.synthetic.publisher.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


class MainActivity : AppCompatActivity() {

    val tag: String = "MainActivity"
    var imageList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val intent = intent
        val action = intent?.action
        val type = intent?.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                //parseDocument(intent.getStringExtra(Intent.EXTRA_TEXT))

                async (UI) {
                    val result = bg { run(intent.getStringExtra(Intent.EXTRA_TEXT)) }
                    result.await()
                }
            }
        }

        fab.setOnClickListener { view ->

            anonymousAuthentication()

        }
    }

    private fun parseDocument(stringExtra: String?) {
        val thread = SimpleThread(stringExtra!!)
        thread.start()
    }

    private fun run(stringExtra: String) {
        try {

            var title: String?
            var description = ""

            Log.d(tag, stringExtra)

            val document: Document = Jsoup.connect(stringExtra).get()

            title = document.title()

            val metaTags = document.getElementsByTag("meta")

            for (metaTag: Element in metaTags) {
                val content = metaTag.attr("content")
                val name = metaTag.attr("name")

                if (name == "og:description" || name == "description") {
                    description = content
                }
            }

            Log.d(tag, title)
            Log.d(tag, description)

            val imageTags = document.getElementsByTag("img")

            for (imageTag: Element in imageTags) {
                val src = imageTag.absUrl("src")
                if (src.contains(".png", true) ||
                        src.contains(".jpg", true) ||
                        src.contains(".jpeg", true)) {
                    Log.d(tag, src)
                    imageList.add(src)
                }
            }

            this@MainActivity.runOnUiThread({
                editTitle.setText(title, TextView.BufferType.EDITABLE)
                editDescription.setText(description, TextView.BufferType.EDITABLE)
                rvDocumentImages.adapter = ImagesAdapter(imageList, this@MainActivity, imageAdapterClickListener)
                rvDocumentImages.layoutManager = android.support.v7.widget.StaggeredGridLayoutManager(2, android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL)
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class SimpleThread(private val stringExtra: String) : Thread() {
        override fun run() {
            try {

                var title: String?
                var description = ""

                Log.d(tag, stringExtra)

                val document: Document = Jsoup.connect(stringExtra).get()

                title = document.title()

                val metaTags = document.getElementsByTag("meta")

                for (metaTag: Element in metaTags) {
                    val content = metaTag.attr("content")
                    val name = metaTag.attr("name")

                    if (name == "og:description" || name == "description") {
                        description = content
                    }
                }

                Log.d(tag, title)
                Log.d(tag, description)

                val imageTags = document.getElementsByTag("img")

                for (imageTag: Element in imageTags) {
                    val src = imageTag.absUrl("src")
                    if (src.contains(".png", true) ||
                            src.contains(".jpg", true) ||
                            src.contains(".jpeg", true)) {
                        Log.d(tag, src)
                        imageList.add(src)
                    }
                }

                this@MainActivity.runOnUiThread({
                    editTitle.setText(title, TextView.BufferType.EDITABLE)
                    editDescription.setText(description, TextView.BufferType.EDITABLE)
                    rvDocumentImages.adapter = ImagesAdapter(imageList, this@MainActivity, imageAdapterClickListener)
                    rvDocumentImages.layoutManager = android.support.v7.widget.StaggeredGridLayoutManager(2, android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL)
                })

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var imageAdapterClickListener = object : ImageAdapterClickListener {
        override fun onImageClickListener(position: Int) {
            Log.d(tag, position.toString())
            imageUri = imageList[position]
            textImageUrl.text = imageUri
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
                Snackbar.make(fab, "Error occured : " + task.exception.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

    var title: String = ""
    var description: String = ""
    var imageUri: String = ""
    private fun uploadData() {
        title = editTitle.text.toString()
        description = editDescription.text.toString()

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
            dbRef.removeEventListener(valueEventListener)
            dbRef.addValueEventListener(valueEventListener)

        } else {
            Snackbar.make(fab, "Enter title and description", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError?) {
            Snackbar.make(fab, "Error occurred : " + databaseError?.message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        override fun onDataChange(p0: DataSnapshot?) {
            Snackbar.make(fab, "Data published", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}
