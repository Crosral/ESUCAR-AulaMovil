package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.QueryRepository
import com.example.myapplication.ui.components.QueryFormModal
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.GreenCarabineros
import com.example.myapplication.ui.viewmodel.QueryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceViewerScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Instanciación segura del ViewModel con Factory
    val queryViewModel: QueryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getDatabase(context)
                val repository = QueryRepository(database.queryDao())
                @Suppress("UNCHECKED_CAST")
                return QueryViewModel(repository) as T
            }
        }
    )

    var showModal by remember { mutableStateOf(false) }
    var isStudied by remember { mutableStateOf(false) }

    if (showModal) {
        QueryFormModal(
            subjectName = "Derecho Procesal Penal",
            unitName = "Unidad 1",
            resourceTitle = "Guía Didáctica - Código Procesal Penal",
            viewModel = queryViewModel,
            onDismiss = { showModal = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visor de Recursos", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenCarabineros)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Documento PDF: Guía Didáctica\n(Vista de lectura del contenido)",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isStudied = !isStudied },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isStudied) Color.Gray else GreenCarabineros
                    )
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isStudied) "Estudiado" else "Marcar estudiado", fontSize = 12.sp)
                }

                Button(
                    onClick = { showModal = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenCarabineros)
                ) {
                    Icon(Icons.Default.QuestionAnswer, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Duda al docente", fontSize = 12.sp)
                }
            }
        }
    }
}