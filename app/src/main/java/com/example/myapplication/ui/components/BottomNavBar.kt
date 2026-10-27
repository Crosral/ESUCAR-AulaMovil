package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.theme.GreenCarabineros

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "dashboard",
            onClick = { onNavigate("dashboard") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenCarabineros,
                selectedTextColor = GreenCarabineros
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Ramos") },
            label = { Text("Ramos") },
            selected = currentRoute == "subject_units" || currentRoute == "ramos",
            onClick = { onNavigate("subject_units") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenCarabineros,
                selectedTextColor = GreenCarabineros
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Download, contentDescription = "Descargas") },
            label = { Text("Descargas") },
            selected = currentRoute == "downloads" || currentRoute == "descargas" || currentRoute == "resource_list",
            onClick = { onNavigate("downloads") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenCarabineros,
                selectedTextColor = GreenCarabineros
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Mail, contentDescription = "Consultas") },
            label = { Text("Consultas") },
            selected = currentRoute == "query_inbox" || currentRoute == "consultas",
            onClick = { onNavigate("query_inbox") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenCarabineros,
                selectedTextColor = GreenCarabineros
            )
        )
        // Corregido: Ahora redirige a "profile" en lugar de "teacher_panel"
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "profile" || currentRoute == "perfil",
            onClick = { onNavigate("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenCarabineros,
                selectedTextColor = GreenCarabineros
            )
        )
    }
}