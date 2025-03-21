package com.example.obirinweather


import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.obirinweather.ui.theme.ObirinWeatherTheme
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ObirinWeatherTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    WeatherScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun WeatherScreen(modifier: Modifier = Modifier){

    Column (modifier = Modifier.fillMaxSize(),
        ){

        //Grey Top Bar
        Surface(modifier = Modifier.fillMaxWidth().padding(),
            color = Color(0xFF935FA7)
        ){
            Text(
                text = "Obirin Weather App",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
                .align(Alignment.CenterHorizontally),
                color = Color.White
            )

        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text="St. Paul, MN",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding()
        )

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)){
            Text (text="72°",
                fontSize = 48.sp)

            Spacer(modifier = Modifier.width(44.dp)) // Adjust spacing evenly as desired

            Image(
                painter = painterResource(R.drawable.sun),
                contentDescription = "Sunny",
                modifier = Modifier.size(100.dp)
            )
        }

        Text(
            text = "Feels like 78°",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column (modifier = Modifier.padding(top = 16.dp)){
            Text("Low 65°", fontSize = 16.sp)
            Text("High 80°", fontSize = 16.sp)
            Text("Humidity 100%", fontSize = 16.sp)
            Text("Pressure 1023 hPa", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ObirinWeatherTheme {
        WeatherScreen()
    }
}