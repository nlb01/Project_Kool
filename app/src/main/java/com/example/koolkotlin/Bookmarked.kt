package com.example.koolkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.koolkotlin.databinding.ActivityHomePageBinding
import com.example.koolkotlin.ui.theme.IngredientListViewModel
import com.example.koolkotlin.ui.theme.RecipeListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

lateinit var all_bookMarks: List<List<String>>


class Bookmarked : ComponentActivity() {
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("saved","Currently in Bookmarked Activity")
        val textView = findViewById<MultiAutoCompleteTextView>(R.id.search_text)
        textView.setText("SORRY! SEARCH DENIED")
        textView.isEnabled = false

        //-----------------Disable Search Functionality for Saved Recipes---------------------//


//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1
//            , all_ingredients)
//        textView.setAdapter(adapter)
//        textView.setOnItemClickListener { parent, view, position, id ->
//            search_ingredients.add(parent.getItemAtPosition(position).toString())
//        }
//
//        textView.threshold = 1
//        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
//
//        textView.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
//            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
//
//                var recipe_ingredients : MutableList<Int> = ArrayList<Int>()
//                for (ingred in  search_ingredients) {
//                    var id = All_ingredients.getIngredientId(ingred)
//                    if(id != -1) {
//                        recipe_ingredients.add(id)
//                    }
//                }
//
//                intent = Intent(this, HomePageCompose::class.java);
//                intent.putExtra("ingredList" , recipe_ingredients.toIntArray())
//                intent.putExtra("bookMark", false)
//                intent.putExtra("type", "none")
//                startActivity(intent);
//
//                return@OnKeyListener true
//            }
//            false
//        }
//        )


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

        addComposeView()
    }


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    private fun addComposeView() {
        binding.uiCompose.setContent {
            setContentSaved()
        }
    }
}


@Composable
fun setContentSaved() {
    Column {
        Log.i("rec", "all recipes is empty while creating columns" + all_recipes.isEmpty().toString())
        for (recipe in all_bookMarks) {

            Log.i("rec", recipe.toString())
            makeIndividualCardSaved(recipe)
        }
    }
}

@Composable
fun makeIndividualCardSaved(recipe : List<String>) {

    val title = recipe[0]
    val duration = recipe [1]
    val type = recipe [2]
    val style = recipe [3]
    var img = recipe[4].replace("IMG(data=", "")
    img = img.replace("[" , "")
    img = img.replace("]" , "")
    img = img.replace(" " , "")
    img = img.replace("type=Buffer)" , "")
    val ingredients = recipe [5]
    val id = recipe[6]

    val charList = img.split(",")
    Log.i("saved", "charList : " + charList.toString() )
    val bite = ArrayList<Byte>()
    for(num in  charList) {
        if (num != "") {
            val num_int = num.toInt()
            val byte = num_int.toByte()
            bite.add(byte)
        }
    }

    val byte_arr = bite.toByteArray()
    val bitmap = BitmapFactory.decodeByteArray(byte_arr , 0 , byte_arr.size)

    PreviewCardSaved(
        bit = bitmap,
        id = id.toInt(),
        time = duration + " minutes",
        Style = style,
        Type = type,
        Ingredients = ingredients,
        Title = title)

    Spacer(modifier = Modifier.height(20.dp))
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PreviewCardSaved(bit: Bitmap, id: Int, time: String, Style: String, Type:String, Ingredients: String, Title: String) {

    val textColor = colorResource(id = R.color.title_color);
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(2.dp , colorResource(R.color.button_outline)),
        onClick = {
            var intent = Intent(context, RecipeDetails::class.java)
            intent.putExtra("Id" , id)
            intent.putExtra("saved" , true)
            context.startActivity(intent)
        }
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier
                .background(colorResource(id = R.color.card_background))
                .padding(10.dp),
        ){
            Row { Modifier.padding(all = 8.dp)
                Image (
                    bitmap = bit.asImageBitmap(),
//                    painter = painterResource(id = R.drawable.pasta),
                    contentDescription = Title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)

                )

                Spacer(modifier = Modifier.width(10.dp))

                Divider(
                    modifier = Modifier
                        .height(82.dp)
                        .padding(end = 34.dp)
                        .width(2.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))


                Column {
                    Text(text = "Time: " + time , color = textColor)
                    Text(text = "Style: " + Style, color = textColor)
                    Text(text = "Cuisine: " + Type, color = textColor)
                    Text(text = "Ingredients: " + Ingredients, color = textColor,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .padding(end = 34.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = Title ,
                color = colorResource(id = R.color.title_color) ,
                fontWeight = FontWeight.Bold
            )


        }

        Spacer(modifier = Modifier.height(10.dp))
    }

}

