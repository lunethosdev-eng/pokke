package com.poke.music.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
                    )
                )
        )

        // Avatar overlapping banner
        Box(
            modifier = Modifier
                .offset(y = (-40).dp)
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("P", style = MaterialTheme.typography.headlineMedium)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                "Usuario",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "@username",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            // Badge placeholder
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF8B5CF6).copy(alpha = 0.2f)
            ) {
                Text(
                    "Owner",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color(0xFFA78BFA),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(24.dp))

            ListItem(
                headlineContent = { Text("Ajustes") },
                leadingContent = { Icon(Icons.Default.Settings, null) }
            )
            ListItem(
                headlineContent = { Text("Cerrar sesión") },
                leadingContent = { Icon(Icons.Default.Logout, null) },
                modifier = Modifier,
                colors = ListItemDefaults.colors(
                    headlineColor = MaterialTheme.colorScheme.error
                )
            )
            // Make logout clickable properly later
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
