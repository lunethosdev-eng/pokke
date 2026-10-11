package com.poke.music.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poke.music.data.model.Song
import com.poke.music.data.repository.MusicRepository
import kotlinx.coroutines.launch

@Composable
fun SearchScreen() {
    val context = LocalContext.current
    val repo = remember { MusicRepository(context) }
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Song>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var downloadProgress by remember { mutableStateOf<Int?>(null) }
    var downloadingId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar canciones, artistas…") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                if (query.isNotBlank()) {
                    TextButton(onClick = {
                        scope.launch {
                            isLoading = true
                            error = null
                            results = emptyList()
                            repo.search(query)
                                .onSuccess { results = it }
                                .onFailure { error = it.message ?: "Error de búsqueda" }
                            isLoading = false
                        }
                    }) {
                        Text("Buscar")
                    }
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            Text(
                "Buscando… Si la canción no está, se descargará en tiempo real.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }

        if (downloadProgress != null) {
            LinearProgressIndicator(
                progress = { (downloadProgress ?: 0) / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Text("Descargando… $downloadProgress%", style = MaterialTheme.typography.bodySmall)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(results, key = { it.id }) { song ->
                SongRow(
                    song = song,
                    isDownloading = downloadingId == song.id,
                    onPlay = { /* TODO: start playback */ },
                    onDownload = {
                        scope.launch {
                            downloadingId = song.id
                            downloadProgress = 0
                            // Collect progress
                            // For simplicity we just call download
                            repo.downloadSong(song)
                                .onSuccess {
                                    downloadProgress = 100
                                }
                                .onFailure {
                                    error = it.message
                                }
                            downloadingId = null
                            downloadProgress = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SongRow(
    song: Song,
    isDownloading: Boolean,
    onPlay: () -> Unit,
    onDownload: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                "${song.artist} • ${song.durationFormatted}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = onPlay),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        },
        trailingContent = {
            IconButton(onClick = onDownload, enabled = !isDownloading) {
                if (isDownloading) {
                    CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Download, contentDescription = "Descargar")
                }
            }
        }
    )
}
