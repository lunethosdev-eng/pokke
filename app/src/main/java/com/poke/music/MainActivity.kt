package com.poke.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.poke.music.data.repository.AuthRepository
import com.poke.music.ui.navigation.PokkeNavGraph
import com.poke.music.ui.theme.PokkeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokkeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val auth = remember { AuthRepository() }
                    PokkeNavGraph(authRepository = auth)
                }
            }
        }
    }
}
