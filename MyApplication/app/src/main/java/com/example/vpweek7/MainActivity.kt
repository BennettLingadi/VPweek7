package com.example.vpweek7

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.soalujian.soal1.view.PokemonApp
import com.example.soalujian.soal2.view.MusicApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            //PokemonApp()      // Soal 1 - Pokemon Card
            MusicApp()     // Soal 2 - Music Artist (TheAudioDB)
        }
    }
}