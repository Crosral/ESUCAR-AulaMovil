package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.GoldAccent
import com.example.myapplication.ui.theme.GreenCarabineros

data class ResourceModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceListScreen(
    onSelectResource: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val resources = listOf(
        ResourceModel("1", "Guía Didáctica - Código Procesal Penal", "PDF · 2.4 MB", "Revisado"),
        ResourceModel("2", "Manual de Detención en Flagrancia", "PDF · 4.1 MB", "Pendiente"),
        ResourceModel("3", "Lectura Obligatoria - Fijación de Sitio", "PDF · 1.8 MB", "Pendiente")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recursos de Aprendizaje", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenCarabineros)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resources) { resource ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectResource(resource.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = GreenCarabineros)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(resource.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(resource.subtitle, color = Color.Gray, fontSize = 12.sp)
                            }
                        }

                        IconButton(onClick = {
                            Toast.makeText(context, "Descargando ${resource.title}...", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Download, contentDescription = "Descargar", tint = GreenCarabineros)
                        }
                    }
                }
            }
        }
    }
}