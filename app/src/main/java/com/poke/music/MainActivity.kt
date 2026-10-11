package com.poke.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.poke.music.ui.navigation.PokkeNavGraph
import com.poke.music.ui.theme.PokkeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PokkeAppRoot()
        }
    }
}

@Composable
fun PokkeAppRoot() {
    PokkeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PokkeNavGraph()
        }
    }
}
