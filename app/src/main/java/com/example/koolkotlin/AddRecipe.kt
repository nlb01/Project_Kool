package com.example.koolkotlin

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.sql.*


class AddRecipe : AppCompatActivity() {
    var pickedPhoto :Uri? = null
    var pickedBitMap : Bitmap? = null
    var imageList : MutableList<Int> = ArrayList()

    private var conn: Connection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var search_ingredients : MutableList<String> = ArrayList<String>()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        //Create IMG Object
        val img = IMG(type = "Loaded", data = listOf(1,2,3,4,5,6,7,8,9,10))

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }


        val textView = findViewById<MultiAutoCompleteTextView>(R.id.search_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1
            , All_ingredients.ingredientNames)
        textView.setAdapter(adapter)
        textView.setOnItemClickListener { parent, view, position, id ->
            search_ingredients.add(parent.getItemAtPosition(position).toString())
        }
        textView.threshold = 1
        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val recipe_in = findViewById<MultiAutoCompleteTextView>(R.id.ingredientsText)
        recipe_in.setAdapter(adapter)
        recipe_in.threshold = 1
        recipe_in.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

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
            intent = Intent(this, Bookmarked::class.java);
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
    fun addRecipe(view : View){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kool.blackab.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIinterface::class.java)
        //create a new recipe object

        //check if imageList is empty
        if(imageList.isEmpty()){
            //if it is empty notify the user and return
            Toast.makeText(this, "Please add an image", Toast.LENGTH_SHORT).show()
            return
        }


        val ingredients = findViewById<EditText>(R.id.ingredientsText).text.toString()
        val ingredientsArray:List<String> = ingredients.split(",").map { it -> it.trim() }


        //create a new recipe object
        val recipe = getFields()
        //list of ints
        val recipe_insert = RecipeIngredients(recipe, ingredientsArray)
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

        val mySnackbar = Snackbar.make(findViewById(R.id.add_layout) , "Recipe added Succesfully!" , Snackbar.LENGTH_SHORT)
        mySnackbar.show()

        val ingreds = listOf(-1)
        intent = Intent(this, HomePageCompose::class.java);
        intent.putExtra("type", "none")
        intent.putExtra("ingredList", ingreds.toIntArray())
        startActivity(intent);
    }


    //function to get all the data from the form and return a recipe object
    fun getFields(): RecipesItem {
        //get information from the text fields
        val title = findViewById<EditText>(R.id.titleText).text.toString()
        val time = findViewById<EditText>(R.id.timeText).text.toString()
        val style = findViewById<EditText>(R.id.styleText).text.toString()
        val type = findViewById<EditText>(R.id.typeText).text.toString()
        val instructions = findViewById<EditText>(R.id.instructionsText).text.toString()

        val notes = findViewById<EditText>(R.id.notesText).text.toString()
        val videoLink = findViewById<EditText>(R.id.videoText).text.toString()
        //check if video link is empty then don't execute next line



        val img = IMG(type = "Buffer", data = imageList)


        val recipe = RecipesItem(recipe_id = 0, Title = title, Duration = time.toInt(), Style = style, Type = type, Steps = instructions, Notes = notes, VID_URL = videoLink, IMG = img, Rating = 0)

        return recipe
    }



    fun addActivity(view : View) {
        intent = Intent(this, AddRecipe::class.java);
        startActivity(intent);
    }


    //function to get a picture from the gallery
    fun getPhoto(view : View){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/png"
        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val uri = data?.data
            //log uri
            Log.d("TAG", "onActivityResult: " + uri.toString())
            //convert into a bitmap
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            val image = stream.toByteArray()

            var lis : MutableList<Int> = ArrayList<Int>()
            for (byte in image) {
                var convert = byte.toInt()
                lis.add(convert)
            }
            //assign list to the imageList
            imageList = lis
            //log image
            Log.d("TAG", "ints List =  "  + lis.toString())
            val textView = findViewById<TextView>(R.id.picText)
            //set textview to the uri of the image
            textView.text = data?.data.toString()
            //print data
        }
    }



    //Helps with getting recipe information
    fun details(view: View) {

        val title = findViewById<EditText>(R.id.titleText).text.toString()
        val time = findViewById<EditText>(R.id.timeText).text.toString()
        val style = findViewById<EditText>(R.id.styleText).text.toString()
        val type = findViewById<EditText>(R.id.typeText).text.toString()

        val instructions = findViewById<EditText>(R.id.instructionsText).text.toString()
        val ingredients = findViewById<EditText>(R.id.ingredientsText).text.toString()
        //Convert to list by splitting

        val notes = findViewById<EditText>(R.id.notesText).text.toString()
        val videoLink = findViewById<EditText>(R.id.videoText).text.toString()
        //check if video link is empty then don't execute next line
        val videoId = videoLink.split("=")[1]

        val ingredientsArray:List<String> = ingredients.split(",").map { it -> it.trim() }

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







    /////Getting Picture From Gallery



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

}