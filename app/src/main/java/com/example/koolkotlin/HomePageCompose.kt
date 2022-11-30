package com.example.koolkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.example.koolkotlin.databinding.ActivityHomePageBinding
import com.example.koolkotlin.ui.theme.IngredientListViewModel
import com.example.koolkotlin.ui.theme.RecipeListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var all_recipes : List<RecipesItem> = ArrayList<RecipesItem>()

class HomePageCompose : ComponentActivity() {

    private lateinit var binding: ActivityHomePageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        All_ingredients.setNames()
        All_types.setNames()
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        addComposeView()
    }
    

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    private fun addComposeView() {
        binding.uiCompose.setContent {
                setContent(viewModel = RecipeListViewModel())
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

    PreviewCard(
        id = recipe.recipe_id,
        time = recipe.Duration.toString() + " minutes",
        Style = recipe.Style,
        Type = recipe.Type,
        Ingredients = ingredientsCombined,
        Title = recipe.Title)

    Spacer(modifier = Modifier.height(20.dp))
}

//private fun setRecipes(recipes: List<RecipesItem>) {
//    all_recipes = recipes
//    Log.i("rec" , "recipes have been assigned globally!")
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun previewType(type: String) {

    val context = LocalContext.current

    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp , colorResource(R.color.title_color)),
        onClick = {context.startActivity(Intent(context, HomePageCompose::class.java))}
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
fun PreviewCard(id: Int , time: String, Style: String, Type:String, Ingredients: String, Title: String) {

    val textColor = colorResource(id = R.color.title_color);
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(2.dp , colorResource(R.color.button_outline)),
        onClick = {
            var intent = Intent(context, RecipeDetails::class.java)
            intent.putExtra("Id" , id)
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
                    painter = painterResource(id = R.drawable.pasta),
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
                    Text(text = "Type: " + Type, color = textColor)
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


//@Composable
//fun PreviewCardArguments() {
//    PreviewCard(time = "30 minutes", Style = "Italian", Type = "Pasta", Ingredients = "Penne Pasta, Chicken,...", Title = "Homemade Spicy Kung Pao Chicken Noodles")
//}










