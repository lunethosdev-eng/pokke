package com.poke.music.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poke.music.data.model.Song
import com.poke.music.data.repository.MusicRepository
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(onSongClick: (Song) -> Unit = {}) {
    val repo = remember { MusicRepository() }
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Song>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var remoteMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(query) {
        if (query.length < 2) {
            results = emptyList()
            remoteMsg = null
            return@LaunchedEffect
        }
        delay(400)
        loading = true
        remoteMsg = null
        val local = repo.searchLocal(query)
        local.onSuccess { list ->
            results = list
            if (list.isEmpty()) {
                remoteMsg = "Buscando en servidor..."
                repo.searchRemote(query).fold(
                    onSuccess = { remoteMsg = "Solicitud enviada al servidor. Puede tardar unos segundos." },
                    onFailure = { remoteMsg = "No encontrado localmente. Servidor: ${it.message}" }
                )
            }
        }
        local.onFailure { remoteMsg = it.message }
        loading = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar canciones, artistas...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(12.dp))

        if (loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        remoteMsg?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(results) { song ->
                ListItem(
                    headlineContent = { Text(song.title, maxLines = 1) },
                    supportingContent = { Text(song.artist, maxLines = 1) },
                    leadingContent = {
                        AsyncImage(
                            model = song.coverUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSongClick(song) },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}
