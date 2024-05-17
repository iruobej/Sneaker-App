package com.example.sneakerspot2

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button //NEEDED TO IMPORT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.sneakerspot2.ui.theme.SneakerSpot2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SneakerSpot2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

data class Trainer(val name: String, val imageRes: Int, val price: String)

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val startColor = Color(0xFF67D3AC) //Start color: 67D3AC
    val endColor = Color(0xFF00FF66)
    val gradientBrush = Brush.verticalGradient( //Be careful of using ( instead of {
        colors = listOf(startColor, endColor),
        startY = 0f, //starts top left
        endY = Float.POSITIVE_INFINITY //ends bottom right
    )
    var text by remember { mutableStateOf("") } //a variable to keep the value of the TextField
    val context = LocalContext.current //get the activity context within a composable function, use "this" instead in an activity
    var inidata by remember { mutableStateOf(emptyArray<String>()) } //an array of String that will contain the dummy weather data, initially empty

    val trainers = listOf(
        Trainer("New Balance", R.drawable.new_balance_2002r_navy, "£110"),
        Trainer("Jordan 4 Bred", R.drawable.jordan_4_bred, "£250"),
        Trainer("Jordan 4 Black Cat", R.drawable.jordan_4_black_cat, "£210")
    )
    Column (modifier = Modifier.padding(16.dp)){
        Box(
            modifier = Modifier
                .background(brush = gradientBrush)
                .padding(15.dp)
                .fillMaxWidth()
        ){
            Row {
                Column {
                    Button(
                        onClick = { },
                        modifier = Modifier.align(Alignment.Start)
                    ){
                        Text(stringResource(R.string.search))
                    }
                }
                Column {
                    TextField(
                        value = text,
                        label = { Text(stringResource(R.string.search_trainer)) },
                        maxLines = 1,
                        onValueChange = {text = it}, //onValueChange to update the state of the TextField synchronously and immediately
                        modifier = modifier
                            //.padding(0.dp, 0.dp, 0.dp, 16.dp)//add padding between child elements
                            //.fillMaxWidth()//make the TextField fill the width of the screen
                            .align(Alignment.End)
                    )
                }
            }
        }
        Text(
            stringResource(R.string.brands),
            fontSize = 24.sp
        )
        Row{
            Column{
                Button(
                    onClick = { },
                    modifier = modifier
                        .padding(0.dp, 0.dp, 12.dp, 0.dp)//add padding between child elements
                ){
                    Text("Nike")
                }
            }
            Column{
                Button(
                    onClick = { },
                    modifier = modifier
                        .padding(0.dp, 0.dp, 12.dp, 0.dp)//add padding between child elements
                ){
                    Text("New Balance")
                }
            }
            Column{
                Button(
                    onClick = { },
                    modifier = modifier
                        .padding(0.dp, 0.dp, 12.dp, 0.dp)//add padding between child elements
                ){
                    Text("Jordan")
                }
            }
        }
        Text(
            text = "For You",
            fontSize = 24.sp
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(trainers.size) {index ->
                TrainerItem(trainers[index])
            }
        }
        Text(
            text = "Trending",
            fontSize = 24.sp
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(trainers.size) {index ->
                TrainerItem(trainers[index])
            }
        }
        Text(
            text = "Latest Drops",
            fontSize = 24.sp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = Color.LightGray)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SneakerSpot2Theme {
        //HomeScreenPreview()
    }
}

@Composable
fun TrainerItem(trainer: Trainer){
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = trainer.imageRes),
            contentDescription = trainer.name,
            modifier = Modifier.height(100.dp),
            contentScale = ContentScale.Crop
        )
        Text(trainer.name, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
        Text(trainer.price, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
    }
}
