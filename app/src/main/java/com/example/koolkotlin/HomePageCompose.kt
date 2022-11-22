package com.example.koolkotlin

import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.koolkotlin.databinding.ActivityHomePageBinding
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.res.ColorStateListInflaterCompat.inflate
//import com.example.koolkotlin.databinding.ActivityHomePageBinding
import com.example.koolkotlin.ui.theme.KoolKotlinTheme
import com.example.koolkotlin.ui.theme.Main_background
import com.example.koolkotlin.ui.theme.card_background

class HomePageCompose : ComponentActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.add.setOnClickListener {
            intent = Intent(this, AddRecipe::class.java);
            startActivity(intent);
        }
        addComposeView()
    }

    fun addComposeView() {
        binding.uiCompose.setContent {
            Column {
                for (i in 1..7) {
                    PreviewCardArguments()
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

}

//@Composable
//fun searchBar() {
//    AndroidView(
//        factory = { context ->
//            val view = LayoutInflater.from(context).inflate(R.layout.RecipeDetails, null, false)
//            val textView = view.findViewById<TextView>(R.id.text)
//
//            // do whatever you want...
//            view // return the view
//        },
//        update = { view ->
//            // Update the view
//        }
//    )
//}



//@Composable
//fun TxtField() {
//    // we are creating a variable for
//    // getting a value of our text field.
//    var inputvalue = remember { mutableStateOf(TextFieldValue()) }
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = colorResource(id = R.color.main_background))
//    )
//    {
//        OutlinedTextField(
//            value = inputvalue.value,
//            onValueChange = { inputvalue.value = it},
//            placeholder = { androidx.compose.material.Text(text = "Enter Ingredients", color = colorResource(
//                id = R.color.title_color)
//            ) },
//            modifier = Modifier
//                .background(colorResource(id = R.color.main_background))
//                .padding(all = 16.dp)
//                .fillMaxWidth(),
//            shape = RoundedCornerShape(30.dp),
//            keyboardOptions = KeyboardOptions(
//                autoCorrect = true,
//                keyboardType = KeyboardType.Text,
//                ),
//            textStyle = TextStyle(
//                color = colorResource(id = R.color.button_outline),
//                ),
//            singleLine = true,
//            maxLines = 1,
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                unfocusedBorderColor = colorResource(id = R.color.button_outline),
//            ),
//        )
//
//        Button(onClick = { /*TODO*/ }) {
//            Text(text = "+" , color = colorResource(id = R.color.title_color))
//        }
//    }
    
    
//}

// @Preview function is use to see preview
// for our composable function in preview section
//@Preview()
//@Composable
//fun DefaultPreview() {
//    TxtField()
//}



//@Preview
//@Composable
//fun SearchAddBar(){
//    Row(
//        modifier = Modifier
//        .background(colorResource(id = R.color.main_background))
//    )
//    {
//
//    }
//}

@Composable
fun PreviewCard(time: String, Style: String, Type:String, Ingredients: String, Title: String) {

    val textColor = colorResource(id = R.color.title_color);

    Card(
        shape = RoundedCornerShape(20.dp)
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
                    Text(text = "Ingredients: " + Ingredients, color = textColor)
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


@Composable
fun PreviewCardArguments() {
    PreviewCard(time = "30 minutes", Style = "Italian", Type = "Pasta", Ingredients = "Penne Pasta, Chicken,...", Title = "Homemade Spicy Kung Pao Chicken Noodles")
}


