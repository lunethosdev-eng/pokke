package com.poke.music.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.poke.music.data.model.Profile
import com.poke.music.data.repository.AuthRepository
import com.poke.music.ui.theme.PokkeBadgeOwner

@Composable
fun ProfileScreen(
    authRepository: AuthRepository,
    onLogout: () -> Unit
) {
    var profile by remember { mutableStateOf<Profile?>(null) }

    LaunchedEffect(Unit) {
        authRepository.getMyProfile().onSuccess { profile = it }
    }

    val name = profile?.displayName ?: profile?.username ?: "Usuario"
    val handle = profile?.username?.let { "@$it" } ?: authRepository.session?.email ?: ""
    val badge = profile?.badge ?: "none"
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "P"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        Box(
            modifier = Modifier
                .offset(y = (-36).dp)
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(initial, style = MaterialTheme.typography.headlineMedium)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(name, style = MaterialTheme.typography.headlineSmall)
            Text(handle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            if (badge == "owner") {
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PokkeBadgeOwner.copy(alpha = 0.15f)
                ) {
                    Text(
                        "Owner",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = PokkeBadgeOwner,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            ListItem(
                headlineContent = { Text("Ajustes") },
                leadingContent = { Icon(Icons.Default.Settings, null) }
            )

            // Solo UNA opción de cerrar sesión
            ListItem(
                headlineContent = { Text("Cerrar sesión") },
                leadingContent = {
                    Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error)
                },
                colors = ListItemDefaults.colors(headlineColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.clickable {
                    authRepository.logout()
                    onLogout()
                }
            )
        }
    }
}
