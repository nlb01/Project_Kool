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
import kotlin.coroutines.coroutineContext

var all_recipes : List<RecipesItem> = ArrayList<RecipesItem>()

class HomePageCompose : ComponentActivity() {

    private lateinit var binding: ActivityHomePageBinding

    lateinit var final_type : String
    lateinit var ingredList : List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        var search_ingredients : MutableList<String> = ArrayList<String>()

        var ingredientIds = intent.getIntArrayExtra("ingredList")
        var display_type = intent.getStringExtra("type")

        if(display_type == null) {
            final_type = "none"
        }
        else {
            final_type = display_type as String
        }

        if(ingredientIds == null) {
            ingredList = listOf(-1)
        }
        else {
//            Arrays.stream(ints).boxed().toList()
            ingredList = ingredientIds.toCollection(ArrayList())
        }

        super.onCreate(savedInstanceState)
        All_ingredients.setNames()
        All_types.setNames()
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView = findViewById<MultiAutoCompleteTextView>(R.id.search_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1
            , All_ingredients.ingredientNames)
        textView.setAdapter(adapter)
        textView.setOnItemClickListener { parent, view, position, id ->
            search_ingredients.add(parent.getItemAtPosition(position).toString())
        }

        textView.threshold = 1
        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        textView.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                var recipe_ingredients : MutableList<Int> = ArrayList<Int>()
                for (ingred in  search_ingredients) {
                    var id = All_ingredients.getIngredientId(ingred)
                    if(id != -1) {
                        recipe_ingredients.add(id)
                    }
                }

                intent = Intent(this, HomePageCompose::class.java);
                intent.putExtra("ingredList" , recipe_ingredients.toIntArray())
                intent.putExtra("bookMark", false)
                intent.putExtra("type", "none")
                startActivity(intent);

                return@OnKeyListener true
            }
            false
        }
        )


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
                setContent(viewModel = RecipeListViewModel(final_type , ingredList))
        }
    }
}

@Composable
fun setContent(viewModel: RecipeListViewModel) {

    Column {
        Row(modifier = Modifier.horizontalScroll(
            rememberScrollState(),
            enabled = true,
            reverseScrolling = true
        )) {
            for(type in All_types.typeNames) {
                previewType(type = type)
                Spacer(modifier = Modifier.width(5.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            val recipes by viewModel.recipes.collectAsState()
            all_recipes = recipes

            Log.i("rec", "all recipes is empty while creating columns" + all_recipes.isEmpty().toString())
            for (recipe in all_recipes) {

                Log.i("rec", recipe.Title)
                makeIndividualCard(viewModel = IngredientListViewModel(recipe.recipe_id), recipe = recipe)
            }
        }
    }
}

@Composable
fun makeIndividualCard(viewModel : IngredientListViewModel , recipe: RecipesItem) {
    val ingredients by viewModel.ingredients.collectAsState()
    var ingredientsCombined = ""
    if(ingredients.size >3) {
        ingredientsCombined = ingredients[0].Name + ", " + ingredients[1].Name + ", " + ingredients[2].Name + ",... "
    }
    else {
        if(ingredients.size !== 0) {
            for(i in 0..ingredients.size-1) {
                if(i == ingredients.size - 1){
                    ingredientsCombined += ingredients[i].Name + "."
                }
                else {
                    ingredientsCombined += ingredients[i].Name + ", "
                }
            }
        }
    }

    val db_pic = recipe.IMG!!.data
    val bite = ArrayList<Byte>()
    for(num in db_pic!!) {
        val num_byte = num.toByte()
        bite.add(num_byte)
    }

    Log.i("pic" , "------------------------------------------------")

    val byte_arr = bite.toByteArray()
    Log.i("pic" , "size = : " +  byte_arr.size.toString())
    var bitmap = BitmapFactory.decodeByteArray(byte_arr , 0 , byte_arr.size)
    if(bitmap == null) {
        bitmap= BitmapFactory.decodeResource(LocalContext.current.resources , R.drawable.index)
    }
    Log.i("pic" , "bitmap = " + bitmap.toString())
    PreviewCard(
        bit = bitmap,
        id = recipe.recipe_id,
        time = recipe.Duration.toString() + " minutes",
        Style = recipe.Style,
        Type = recipe.Type,
        Ingredients = ingredientsCombined,
        Title = recipe.Title)

    Spacer(modifier = Modifier.height(20.dp))
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun previewType(type: String) {

    val context = LocalContext.current

    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp , colorResource(R.color.title_color)),
        onClick = {
            var intent = Intent(context, HomePageCompose::class.java)
            intent.putExtra("type" , type)
            context.startActivity(intent)
        }
    ) {
        Text(
            text = type,
            modifier = Modifier
                .background(colorResource(id = R.color.card_background))
                .padding(10.dp),
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PreviewCard(bit: Bitmap, id: Int, time: String, Style: String, Type:String, Ingredients: String, Title: String) {

    val textColor = colorResource(id = R.color.title_color);
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(2.dp , colorResource(R.color.button_outline)),
        onClick = {
            var intent = Intent(context, RecipeDetails::class.java)
            intent.putExtra("Id" , id)
            intent.putExtra("saved" , false)
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

