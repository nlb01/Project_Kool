package com.example.koolkotlin

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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

    val days = arrayOf<String>("monday" , "tuesday", "thursday" , "sunnday", "sunday", "sundaaaay")
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView = findViewById<MultiAutoCompleteTextView>(R.id.search_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1
            , days)
        textView.setAdapter(adapter)
        textView.threshold = 1
        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val add = findViewById<ImageButton>(R.id.add)
        add.setOnClickListener {
            add.setBackgroundResource(R.drawable.button_clicked)
            intent = Intent(this, AddRecipe::class.java);
            startActivity(intent);
        }

        addComposeView()
    }
    

    private fun addComposeView() {
        binding.uiCompose.setContent {
            setContent()
        }
    }

}

@Composable
fun setContent() {
    Column {
        Row(modifier = Modifier.horizontalScroll(rememberScrollState(),enabled = true, reverseScrolling = true, )) {
            for (i in 1..7) {
                previewType(type = "Pasta")
                Spacer(modifier = Modifier.width(5.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            for (i in 1..7) {
                PreviewCardArguments()
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}




@Composable
fun previewType(type: String) {

    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp , colorResource(R.color.title_color))
    ) {
        Text(
            text = "Pasta",
            modifier = Modifier
                .background(colorResource(id = R.color.card_background))
                .padding(10.dp),)
    }
}


@Composable
fun PreviewCard(time: String, Style: String, Type:String, Ingredients: String, Title: String) {

    val textColor = colorResource(id = R.color.title_color);

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(2.dp , colorResource(R.color.button_outline))
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


