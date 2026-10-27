package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.GreenCarabineros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectUnitsScreen(
    subjectCode: String,
    onNavigateBack: () -> Unit,
    onSelectUnit: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Derecho Procesal Penal ($subjectCode)", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenCarabineros)
            )
        }
    ) { padding ->
        val units = listOf(
            "Unidad 1: Generalidades del Proceso Penal",
            "Unidad 2: Etapa de Investigación y Primeras Diligencias",
            "Unidad 3: Medidas Cautelares y Detención",
            "Unidad 4: Juicio Oral y Recursos"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(units) { unitTitle ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectUnit(unitTitle) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Folder, contentDescription = null, tint = GreenCarabineros)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(unitTitle, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}