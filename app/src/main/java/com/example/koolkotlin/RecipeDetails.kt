package com.example.koolkotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView

class RecipeDetails : AppCompatActivity() {

    var mediaControls: MediaController? = null
    var vid: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        vid = findViewById<View>(R.id.vid) as VideoView

        if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(this)
            // set the anchor view for the video view
            mediaControls!!.setAnchorView(this.vid)
        }

        // set the media controller for video view
        vid!!.setMediaController(mediaControls)

        // set the absolute path of the video file which is going to be played
        vid!!.setVideoURI(
            Uri.parse("android.resource://"
                + packageName + "/" + R.raw.testvideo ))

        vid!!.requestFocus()
        vid!!.start()

    }


    fun comment(view: View) {
        intent = Intent(this, RecipeDetails::class.java);
        startActivity(intent);
    }

    fun addActivity(view: View) {
        intent = Intent(this, AddRecipe::class.java);
        startActivity(intent);
    }
}