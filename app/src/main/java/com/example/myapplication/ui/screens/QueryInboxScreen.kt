package com.example.myapplication.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.QueryEntity
import com.example.myapplication.data.repository.QueryRepository
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.GoldAccent
import com.example.myapplication.ui.theme.GreenCarabineros
import com.example.myapplication.ui.viewmodel.QueryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueryInboxScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { QueryRepository(database.queryDao()) }
    val viewModel = remember { QueryViewModel(repository) }

    val queries by viewModel.allQueries.collectAsState(initial = emptyList())

    var showBottomSheet by remember { mutableStateOf(false) }

    var subjectName by remember { mutableStateOf("Derecho Procesal Penal") }
    var subjectText by remember { mutableStateOf("") }
    var detailText by remember { mutableStateOf("") }
    var capturedPhoto by remember { mutableStateOf<Bitmap?>(null) }

    var subjectTextError by remember { mutableStateOf(false) }
    var detailTextError by remember { mutableStateOf(false) }

    // 1. Launcher para abrir la Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedPhoto = bitmap
            Toast.makeText(context, "Foto capturada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    // 2. Launcher para solicitar Permisos en Tiempo de Ejecución (IE 2.4.1)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(context, "No se pudo abrir la cámara en este dispositivo", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Debes conceder permiso para usar la cámara", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buzón de Consultas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenCarabineros)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = GreenCarabineros,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Consulta")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(padding)
        ) {
            if (queries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No has enviado consultas todavía.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(queries) { query ->
                        QueryCard(query = query)
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    capturedPhoto = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Nueva Consulta al Docente",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenCarabineros
                    )

                    OutlinedTextField(
                        value = subjectText,
                        onValueChange = {
                            subjectText = it
                            subjectTextError = it.isBlank()
                        },
                        label = { Text("Asunto o Título de la Consulta") },
                        isError = subjectTextError,
                        supportingText = {
                            if (subjectTextError) {
                                Text("El asunto es requerido", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = detailText,
                        onValueChange = {
                            detailText = it
                            detailTextError = it.isBlank()
                        },
                        label = { Text("Detalle o Mensaje") },
                        isError = detailTextError,
                        supportingText = {
                            if (detailTextError) {
                                Text("El detalle es requerido", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                // Comprobación segura de permisos antes de abrir la cámara
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    try {
                                        cameraLauncher.launch(null)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error al abrir la cámara del emulador", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GreenCarabineros)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Adjuntar Foto", color = GreenCarabineros)
                        }

                        if (capturedPhoto != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = GreenCarabineros)
                                Text("Foto Lista", fontSize = 12.sp, color = GreenCarabineros, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    capturedPhoto?.let { bmp ->
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "Vista previa",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            subjectTextError = subjectText.isBlank()
                            detailTextError = detailText.isBlank()

                            if (subjectTextError || detailTextError) {
                                Toast.makeText(context, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            scope.launch {
                                val newQuery = QueryEntity(
                                    subjectName = subjectName,
                                    unitName = "General",
                                    resourceTitle = "Consulta Directa",
                                    subjectText = subjectText,
                                    detailText = detailText,
                                    status = "Pendiente"
                                )
                                database.queryDao().insertQuery(newQuery)
                                Toast.makeText(context, "Consulta enviada correctamente", Toast.LENGTH_SHORT).show()

                                subjectText = ""
                                detailText = ""
                                capturedPhoto = null
                                showBottomSheet = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenCarabineros),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar Consulta")
                    }
                }
            }
        }
    }
}

@Composable
fun QueryCard(query: QueryEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(query.subjectName, fontSize = 12.sp, color = GreenCarabineros, fontWeight = FontWeight.Bold)
                AssistChip(
                    onClick = { },
                    label = { Text(query.status, fontSize = 11.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = GoldAccent.copy(alpha = 0.2f),
                        labelColor = GreenCarabineros
                    )
                )
            }
            Text(query.subjectText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(query.detailText, fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}