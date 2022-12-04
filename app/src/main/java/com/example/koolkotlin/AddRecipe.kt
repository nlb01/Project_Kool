package com.example.koolkotlin

import android.app.Activity
import java.sql.*
import java.util.Properties
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.MultiAutoCompleteTextView
import androidx.compose.foundation.Image
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.koolkotlin.ui.theme.DBConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

class AddRecipe : AppCompatActivity() {
    var pickedPhoto :Uri? = null
    var pickedBitMap : Bitmap? = null
//    val days = arrayOf<String>("monday" , "tuesday", "thursday" , "sunnday", "sunday", "sundaaaay")
    private var conn: Connection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        addRecipe()
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

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


    //function to add an ingredient to the database
    fun addIngredient1(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kool.blackab.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIinterface::class.java)
        //create a new ingredient object
        val ingredient = Ingredient(Name = "Melon", Ingredient_id = 0)
        val call = service.addIngredient(ingredient)
        //add the ingredient to the database
        call.enqueue(object : retrofit2.Callback<Ingredient> {
            override fun onResponse(call: retrofit2.Call<Ingredient>, response: retrofit2.Response<Ingredient>) {
                if (response.isSuccessful) {
                    val ingredient = response.body()
                    Log.d("TAG", "onResponse: " + ingredient.toString())
                }
            }

            override fun onFailure(call: retrofit2.Call<Ingredient>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }
        })


    }


    //function to add a recipe to the database
    fun addRecipe(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kool.blackab.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIinterface::class.java)
        //create a new recipe object
        val recipe = RecipesItem(Duration = 1, IMG = null, Notes = "This is a note", Rating = 3, Steps = "This is a step", Type = "This is a type", Style = "This is a style", Title = "This is a Title", recipe_id = 0, VID_URL = "This is a url vid")
        //list of ints
        val ingredients = listOf(66)
        val recipe_insert = RecipeIngredients(recipe, ingredients)
        val call = service.addRecipe(recipe_insert)
        //add the recipe to the database
        call.enqueue(object : retrofit2.Callback<RecipesItem> {
            override fun onResponse(call: retrofit2.Call<RecipesItem>, response: retrofit2.Response<RecipesItem>) {
                if (response.isSuccessful) {
                    val recipe = response.body()
                    Log.d("TAG", "onResponse: " + recipe.toString())
                }
            }

            override fun onFailure(call: retrofit2.Call<RecipesItem>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }
        })
    }




//    fun executeMySQLQuery(byteArray: ByteArray) {
//        var stmt: Statement? = null
//        var resultset: ResultSet? = null
//
//        try {
//            stmt = conn!!.createStatement()
//            val sql = "Insert into Test_Image (ID , Bitmap) Values (?, ?)"
//
//            val prepstmt = conn!!.prepareStatement(sql)
//
//            prepstmt.setInt(1, 1)
////            prepstmt.setBlob(2, byteArray)
//
//
////            while (resultset!!.next()) {
////                Log.i("DB" , "Database found" + resultset.getString("Database"))
////            }
//        } catch (ex: SQLException) {
//            Log.i("DB" , "SQL Exception found executing statement!")
//            ex.printStackTrace()
//            Log.i("DB" , "Trace")
//        } finally {
//            // release resources
//            if (resultset != null) {
//                try {
//                    resultset.close()
//                } catch (sqlEx: SQLException) {
//                }
//
//                resultset = null
//            }
//
//            if (stmt != null) {
//                try {
//                    stmt.close()
//                } catch (sqlEx: SQLException) {
//                }
//
//                stmt = null
//            }
//
//            if (conn != null) {
//                try {
//                    conn!!.close()
//                } catch (sqlEx: SQLException) {
//                }
//                conn = null
//            }
//        }
//    }


//    fun getConnection() {
//        val connectionProps = Properties()
//        connectionProps.put("user", DBConstants.USER)
//        connectionProps.put("password", DBConstants.PASS)
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance()
//            conn = DriverManager.getConnection("jdbc:" + "mysql" + "://"
//                + DBConstants.HOST + ":" + DBConstants.PORT + "/" + "",
//            connectionProps)
//
//            //"jdbc:" + "mysql" + "://" +
//            //                    DBConstants.HOST +
//            //            ":" + DBConstants.PORT + "/" +
//            //            ""
//
//            Log.i("DB" , "Database Connection Successful")
//        } catch (ex: SQLException) {
//            Log.i("DB" , "SQL Exception Found! Database Connection Failed")
//            ex.printStackTrace()
//            Log.i("DB" , "This was the stack trace")
//        } catch (ex: Exception) {
//            Log.i("DB" , "Exception Found! Database Connection Failed")
//            ex.printStackTrace()
//            Log.i("DB" , "This was the stack trace")
//        }
//    }


    fun addActivity(view : View) {
        intent = Intent(this, AddRecipe::class.java);
        startActivity(intent);
    }

//    fun uploadPicture(view: View) {
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE) , 1)
//        }
//
//        else {
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent , 2)
//
//        }
//
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent , 2)
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
//            pickedPhoto = data.data
//            if(Build.VERSION.SDK_INT >= 28) {
//                val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
//                pickedBitMap = ImageDecoder.decodeBitmap(source)
//            }
//            else {
//                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver , pickedPhoto)
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    fun details(view: View) {

        val title = findViewById<EditText>(R.id.titleText).text.toString()
        val time = findViewById<EditText>(R.id.timeText).text.toString()
        val style = findViewById<EditText>(R.id.styleText).text.toString()
        val type = findViewById<EditText>(R.id.typeText).text.toString()

        val instructions = findViewById<EditText>(R.id.instructionsText).text.toString()
        val ingredients = findViewById<EditText>(R.id.ingredientsText).text.toString()
        val notes = findViewById<EditText>(R.id.notesText).text.toString()
        val videoLink = findViewById<EditText>(R.id.videoText).text.toString()

        val videoId = videoLink.split("=")[1]
        val ingredientsArray:List<String> = ingredients.split(",").map { it -> it.trim() }
        val instructionsArray:List<String> = instructions.split("\n")

//        val stream: ByteArrayOutputStream = ByteArrayOutputStream()
//        if(pickedBitMap != null) {
//            Log.i("bitmap" , "Bitmap Not null!!")
//        }
//        pickedBitMap?.compress(Bitmap.CompressFormat.PNG , 100, stream)
//        val byteArray = stream.toByteArray()

//        thread(start = true) {
//            getConnection()
//            executeMySQLQuery(byteArray)
//        }
//        intent = Intent(this, TestActivity::class.java);
//        intent.putExtra("title", byteArray)
//        Log.i("bitmap" , "Attached Byte Array to Intent")
//        startActivity(intent);
    }
}