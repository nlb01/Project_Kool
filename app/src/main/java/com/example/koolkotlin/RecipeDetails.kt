package com.example.koolkotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.android.synthetic.main.activity_recipe_details.*
import java.io.BufferedReader
import java.io.InputStreamReader

class RecipeDetails : YouTubeBaseActivity() {
    val days = arrayOf<String>("monday" , "tuesday", "thursday" , "sunnday", "sunday", "sundaaaay")


    val api_key = "AIzaSyCH9vxYf1w9z7XxsFmaMLy8uJImEikYU_c"

    lateinit var ytPlayerInit : YouTubePlayer.OnInitializedListener

    val url = URL("https://kool.blackab.repl.co/ingredients")


//    with(url.openConnection() as HttpURLConnection) {
//        requestMethod = "GET"  // optional default is GET
//
//        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
//
//        inputStream.bufferedReader().use {
//            it.lines().forEach { line ->
//                println(line)
//            }
//        }
//    }



//    val conn = url.openConnection() as HttpURLConnection


//    conn.requestMethod = "GET"

//    BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
//        var line: String?
//        while (br.readLine().also { line = it } != null) {
//            println(line)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

//        if (supportActionBar != null) {
//            supportActionBar!!.hide()
//        }

        val apiResponse = url.readText()
        var gson = Gson()

//        val data = gson.fromJson(apiResponse , )


        val textView = findViewById<MultiAutoCompleteTextView>(R.id.search_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1
            , days)
        textView.setAdapter(adapter)
        textView.threshold = 1
        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val add = findViewById<ImageButton>(R.id.add)
        val bookMark = findViewById<ImageButton>(R.id.bookMark)

        val scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down)
        val scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up)

        bookMark.setOnTouchListener(
            View.OnTouchListener {
                    view ,
                    motionEvent -> when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    bookMark.startAnimation(scale_down)
                    bookMark.performClick()
                    bookMark.setBackgroundResource(R.drawable.ring_button)
                }
                MotionEvent.ACTION_DOWN -> {
                    bookMark.startAnimation(scale_up)
                    bookMark.setBackgroundResource(R.drawable.button_clicked)
                }
            }
                return@OnTouchListener true
            }
        )

        add.setOnTouchListener(
            View.OnTouchListener {
                    view ,
                    motionEvent -> when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    add.startAnimation(scale_down)
                    add.performClick()
                    add.setBackgroundResource(R.drawable.ring_button)
                }
                MotionEvent.ACTION_DOWN -> {
                    add.startAnimation(scale_up)
                    add.setBackgroundResource(R.drawable.button_clicked)
                }
            }
                return@OnTouchListener true
            }
        )

        bookMark.setOnClickListener {
            intent = Intent(this, HomePageCompose::class.java);
            intent.putExtra("bookMark" , true)
            startActivity(intent);
        }

        add.setOnClickListener {
            intent = Intent(this, AddRecipe::class.java);
            startActivity(intent);
        }

        val ytPLayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)

        ytPlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i("YT" , "Video about to be loaded!")
                player?.loadVideo("dQw4w9WgXcQ")
                Log.i("YT" , "Video loaded!")
                player?.play()
                Log.i("YT" , "Video Played!")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i("YT" , "Video Initialization Error!")
                Toast.makeText(this@RecipeDetails, "Video Player Failed", Toast.LENGTH_SHORT)
            }
        }

        ytPLayer.initialize(api_key , ytPlayerInit)

    }


    fun comment(view: View) {
        intent = Intent(this, RecipeDetails::class.java);
        startActivity(intent);
    }

}