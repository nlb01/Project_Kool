package com.example.koolkotlin

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.koolkotlin.ui.theme.RecipeListViewModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.android.synthetic.main.activity_recipe_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class RecipeDetails : YouTubeBaseActivity() {
    val api_key = "AIzaSyCH9vxYf1w9z7XxsFmaMLy8uJImEikYU_c"

    lateinit var ytPlayerInit : YouTubePlayer.OnInitializedListener
    lateinit var ingredients : List<Ingredient>
    lateinit var recipe : RecipesItem
    val url = URL("https://kool.blackab.repl.co/ingredients")


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var id: Int = intent.getIntExtra("Id", -1)
        setContentView(R.layout.activity_recipe_details)

        getIngredients(id)
        getRecipe(id)


        val textView = findViewById<MultiAutoCompleteTextView>(R.id.search_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1
            , All_ingredients.ingredientNames)
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

    }


    fun setVideo(vid_id: String) {
        var vid = "dQw4w9WgXcQ"
        if (vid_id != null) {
            vid = vid_id
        }
        val ytPLayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)
        ytPlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideo(vid)
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@RecipeDetails, "Video Player Failed", Toast.LENGTH_SHORT)
            }
        }
        ytPLayer.initialize(api_key , ytPlayerInit)
        Log.i("ingred" , "video set successfully!")
        Log.i("ingred" , "video id: " + vid)
    }

    fun comment(view: View) {
        intent = Intent(this, RecipeDetails::class.java);
        startActivity(intent);
    }

    fun saveRecipe() {

    }

    fun getRecipe(id: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kool.blackab.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIinterface::class.java)
        val call = service.getRecipe(id)

        call.enqueue(object : Callback<List<RecipesItem>> {
            override fun onResponse(call: Call<List<RecipesItem>>, response: Response<List<RecipesItem>>) {
                if (response.isSuccessful) {
                    val recipes = response.body()
                    Log.i("rec" , "recipes have been fetched!")
                    if (recipes != null) {
                        Log.i("rec" , "recipes not null and are being assigned globally!")
                        recipe = recipes[0]
                        val recipe_picture = findViewById<AppCompatImageView>(R.id.recipe_pic)
                        val db_pic = recipe.IMG.data
                        val bite = ArrayList<Byte>()
                        for(num in db_pic) {
                            val num_byte = num.toByte()
                            bite.add(num_byte)
                        }
                        val byte_arr = bite.toByteArray()
                        Log.i("pic", "It is converted to bytes Array " + byte_arr)
                        val bitmap = BitmapFactory.decodeByteArray(byte_arr , 0 , byte_arr.size)
                        Log.i("pic", "It is converted to a bitmap " + bitmap)
                        findViewById<AppCompatTextView>(R.id.recipe_title).text = recipe.Title
                        findViewById<TextView>(R.id.recipe_time).text = "Time: " + recipe.Duration.toString() + " minutes"
                        findViewById<AppCompatTextView>(R.id.recipe_notes).text = recipe.Notes
                        findViewById<TextView>(R.id.recipe_style).text = "Cuisine: " + recipe.Style
                        findViewById<TextView>(R.id.recipe_type).text = "Type: " + recipe.Type
                        var instructions = "  *  " + recipe.Steps.replace("\n", "\n  *  ")
                        findViewById<AppCompatTextView>(R.id.recipe_instructions).text = instructions
                        findViewById<RatingBar>(R.id.ratingBar).rating = recipe.Rating.toFloat()
                        recipe_picture.setImageBitmap(bitmap)
                        Log.i("pic", "resource set to bitmap ")
                        val video = recipe.VID_URL.split("=")[1]
                        setVideo(video)
                    }
                }
            }

            override fun onFailure(call: Call<List<RecipesItem>>, t: Throwable) {
                Log.d("rec", "error")
            }

        })
    }

    fun getIngredients(id: Int) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://kool.blackab.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIinterface::class.java)
        val call = service.getRecipeIngredients(id)

        call.enqueue(object : Callback<List<Ingredient>> {
            override fun onResponse(call: Call<List<Ingredient>>, response: Response<List<Ingredient>>) {
                if (response.isSuccessful) {
                    var recipe_ingredients = ""
                    ingredients = response.body()!!
                    if (ingredients != null) {
                        for (i in 0..ingredients.size - 1) {
                            if( i == ingredients.size - 1) {
                                recipe_ingredients += ingredients[i].Name + "."
                            }
                            else {
                                recipe_ingredients += ingredients[i].Name + ", "
                            }
                        }
                    }
                    findViewById<AppCompatTextView>(R.id.recipe_ingredients).text = recipe_ingredients
                }
            }

            override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                Log.d("rec", "error")
            }
        })
    }
}


