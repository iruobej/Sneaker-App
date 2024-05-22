package com.example.sneakerspot

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
//import androidx.compose.material3.icons.Filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button //NEEDED TO IMPORT
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.sneakerspot.ui.theme.TrainerViewModel
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import com.example.sneakerspot.ui.theme.SneakerSpotTheme
import com.example.sneakerspot.ui.theme.Trainer

class MainActivity : ComponentActivity() {
    private val trainerViewModel : TrainerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SneakerSpotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController, trainerViewModel)
                        }
                        composable(
                            route = "trainer_detail/{trainerName}",
                            arguments = listOf(navArgument("trainerName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val trainerName = backStackEntry.arguments?.getString("trainerName")
                            TrainerDetailScreen(navController, trainerViewModel, trainerName)
                        }

                    }
                }
            }
        }
    }


    }


    @Composable
    fun HomeScreen(navController: NavController, trainerViewModel: TrainerViewModel, modifier: Modifier = Modifier) {
        val trainers by trainerViewModel.allTrainers.observeAsState(initial = emptyList())
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            Column(modifier = Modifier
                .weight(1f)
                .padding(16.dp)) {
                val brands = listOf("Nike", "New Balance", "Jordan", "Adidas")
                Text(
                    stringResource(R.string.brands),
                    fontSize = 24.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(brands) { brand ->
                        Button(onClick = {}) {
                            Text(brand)
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "For You",
                            fontSize = 24.sp
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(trainers) { trainer ->
                                TrainerItem(trainer) {
                                    navController.navigate("trainer_detail/${trainer.name}")
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Trending",
                            fontSize = 24.sp
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(trainers) { trainer ->
                                TrainerItem(trainer) {
                                    navController.navigate("trainer_detail/${trainer.name}")
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Latest Drops",
                            fontSize = 24.sp
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            for (row in trainers.chunked(2)) {
                                for (trainer in row) {
                                    TrainerItem(trainer){
                                        navController.navigate("trainer_detail/${trainer.name}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            NavBar(navController)
        }
    }


    @Composable
    fun TrainerDetailScreen(navController: NavController, trainerViewModel: TrainerViewModel,  trainerName: String?) {
        var showDialog by remember {mutableStateOf(false)}
        fun shareLink() {
            val shareText = "Hey, check out this sneaker I found on SneakerSpot! " +
                    "https://www.laced.com/products/air-jordan-4-og-bred-reimagined?" +
                    "eu_size=44&preferred_currency=GBP&preferred_location=GB&size_system=UK&uk_size=9&us_size=10&&" +
                    "utm_medium=ppc&utm_source=google&utm_term=&campaignid=18247925162&adgroupid=&" +
                    "utm_campaign=PMax+-+DISC+-+UK+-+Jordan+4&gad_source=1&gclid=Cj0KCQjw6auyBhDzARIsALIo6v_xW8MMyTGkSxblG5NgfekcNQU4vNrgJSwAgaPnJBO8r8BBxXyQk20aAuIVEALw_wcB"
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            navController.context.startActivity(shareIntent)
        }
        val trainer = trainerViewModel.allTrainers.observeAsState().value?.find { it.name == trainerName }
        Column(modifier = Modifier.fillMaxSize()){
            TopBar()
            Column(modifier = Modifier
                .weight(1f) //To push nav bar to bottom of the screen
                .padding(16.dp)) {

                if (trainer != null) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(trainer.name, fontSize = 24.sp, modifier = Modifier.padding(top = 8.dp))
                        Image(
                            painter = painterResource(id = trainer.imageRes),
                            contentDescription = trainer.name,
                            modifier = Modifier.height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            trainer.price,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Button(
                            onClick = {
                                if (trainer.name == "Jordan 4 Bred") {
                                    showDialog = true
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .height(70.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF67D3AC)
                            )
                        ){
                            Text(
                                stringResource(R.string.go_to_site),
                                color = Color.White,
                                fontSize = 30.sp
                            )
                        }
                        Button(
                            onClick = {
                                if (trainer.name == "Jordan 4 Bred") {
                                    shareLink()
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .height(70.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray
                            )
                        ){
                            Text(
                                stringResource(R.string.share),
                                fontSize = 30.sp
                            )
                        }
                    }
                }
            }
            NavBar(navController)
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        val url = "https://www.laced.com/products/air-jordan-4-og-bred-reimagined?eu_size=44&preferred_currency=GBP&preferred_location=GB&size_system=UK&uk_size=9&us_size=10&&utm_medium=ppc&utm_source=google&utm_term=&campaignid=18247925162&adgroupid=&utm_campaign=PMax+-+DISC+-+UK+-+Jordan+4&gad_source=1&gclid=Cj0KCQjw6auyBhDzARIsALIo6v_xW8MMyTGkSxblG5NgfekcNQU4vNrgJSwAgaPnJBO8r8BBxXyQk20aAuIVEALw_wcB"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        navController.context.startActivity(intent)
                    }){
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                },
                title = { Text("Permission Required")},
                text = { Text("Do you want give SneakerSpot permission to open the browser?")}
            )
        }
    }
    @Composable
    fun TopBar (modifier: Modifier = Modifier){
        val startColor = Color(0xFF67D3AC) //Start color: 67D3AC
        val endColor = Color(0xFF00FF66)
        val gradientBrush = Brush.verticalGradient( //Be careful of using ( instead of {
            colors = listOf(startColor, endColor),
            startY = 0f, //starts top left
            endY = Float.POSITIVE_INFINITY //ends bottom right
        )
        var text by remember { mutableStateOf("") } //a variable to keep the value of the TextField
        val context =
            LocalContext.current //get the activity context within a composable function, use "this" instead in an activity
        //var inidata by remember { mutableStateOf(emptyArray<String>()) } //an array of String that will contain the dummy weather data, initially empty


        Box(
            modifier = Modifier
                .background(brush = gradientBrush)
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Row {
                Column {
                    Button(
                        onClick = { },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text(stringResource(R.string.search))
                    }
                }
                Column {
                    TextField(
                        value = text,
                        label = { Text(stringResource(R.string.search_trainer)) },
                        maxLines = 1,
                        onValueChange = {
                            text = it
                        }, //onValueChange to update the state of the TextField synchronously and immediately
                        modifier = modifier
                            //.padding(0.dp, 0.dp, 0.dp, 16.dp)//add padding between child elements
                            //.fillMaxWidth()//make the TextField fill the width of the screen
                            .align(Alignment.End)
                    )
                }
            }
        }
    }

    @Composable
    fun NavBar (navController: NavController) {
        //Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFD3D3D3)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("home")}) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_home_24),
                    contentDescription = "Home"
                )
            }
            //Favorited Items
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.heart_70),
                    contentDescription = "Heart"
                )
            }
        }
    }
    @Composable
    fun TrainerItem(trainer: Trainer, onItemClick: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable(onClick = onItemClick), //To respond to click events
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = trainer.imageRes),
                contentDescription = trainer.name,
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.Crop
            )
            Text(trainer.name, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
            Text(
                trainer.price,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

