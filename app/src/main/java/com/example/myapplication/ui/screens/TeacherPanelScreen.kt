package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.QueryEntity
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.GoldAccent
import com.example.myapplication.ui.theme.GreenCarabineros
import com.example.myapplication.ui.theme.GreenCarabinerosLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPanelScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val queryDao = remember { database.queryDao() }

    // Obtener las consultas en tiempo real desde Room
    val queries by queryDao.getAllQueries().collectAsState(initial = emptyList())

    // Filtro de lista (Todas, Pendientes, Respondidas)
    var selectedFilter by remember { mutableStateOf("Todas") }

    val filteredQueries = when (selectedFilter) {
        "Pendientes" -> queries.filter { it.status.equals("Pendiente", ignoreCase = true) }
        "Respondidas" -> queries.filter { it.status.equals("Respondida", ignoreCase = true) }
        else -> queries
    }

    val pendingCount = queries.count { it.status.equals("Pendiente", ignoreCase = true) }
    val answeredCount = queries.count { it.status.equals("Respondida", ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portal Docente - ESUCAR", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver / Salir", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión", tint = GoldAccent)
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
        ) {
            // Header del Docente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenCarabineros)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text("Mayor Juan Pérez", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Docente de Derecho Procesal Penal", color = GoldAccent, fontSize = 13.sp)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Tarjetas de Métricas de Gestión
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Pendientes",
                        count = pendingCount.toString(),
                        icon = Icons.Default.PendingActions,
                        containerColor = Color(0xFFFFF3E0),
                        contentColor = Color(0xFFE65100),
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Respondidas",
                        count = answeredCount.toString(),
                        icon = Icons.Default.MarkEmailRead,
                        containerColor = Color(0xFFE8F5E9),
                        contentColor = GreenCarabineros,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filtros de navegación rápida
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null, tint = GreenCarabineros)
                    Text("Filtrar:", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    listOf("Todas", "Pendientes", "Respondidas").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenCarabineros,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("CONSULTAS DE ALUMNOS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenCarabineros)
                Spacer(modifier = Modifier.height(8.dp))

                if (filteredQueries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay consultas en esta sección.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredQueries) { query ->
                            TeacherQueryCard(
                                query = query,
                                onMarkAsAnswered = {
                                    scope.launch {
                                        val updatedQuery = query.copy(status = "Respondida")
                                        queryDao.updateQuery(updatedQuery)
                                        Toast.makeText(context, "Consulta marcada como Respondida", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    count: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, fontSize = 11.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
                Text(count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = contentColor)
            }
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun TeacherQueryCard(
    query: QueryEntity,
    onMarkAsAnswered: () -> Unit
) {
    val isPending = query.status.equals("Pendiente", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(query.subjectName, fontSize = 12.sp, color = GreenCarabinerosLight, fontWeight = FontWeight.Bold)
                AssistChip(
                    onClick = {},
                    label = { Text(query.status, fontSize = 11.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isPending) Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                        labelColor = if (isPending) Color(0xFFE65100) else GreenCarabineros
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(query.subjectText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(query.detailText, fontSize = 13.sp, color = Color.DarkGray)

            if (isPending) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onMarkAsAnswered,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenCarabineros),
                    modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Marcar Respondida", fontSize = 12.sp)
                }
            }
        }
    }
}