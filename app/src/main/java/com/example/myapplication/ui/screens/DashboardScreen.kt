package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.BottomNavBar
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.GoldAccent
import com.example.myapplication.ui.theme.GreenCarabineros
import com.example.myapplication.ui.theme.GreenCarabinerosLight

@Composable
fun DashboardScreen(
    onNavigateToSubject: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateBottomBar: (String) -> Unit,
    onLogout: () -> Unit // Callback para cerrar sesión
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "dashboard",
                onNavigate = onNavigateBottomBar
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Institucional ESUCAR con botón de Cerrar Sesión
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenCarabineros)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ESUCAR - Aula Móvil",
                            color = GoldAccent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cabo 2do. González",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "2° Escuadrón · Promoción 2024",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    Row {
                        IconButton(onClick = onNavigateToNotifications) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = Color.White
                            )
                        }
                        // Botón para Cerrar Sesión
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar Sesión",
                                tint = GoldAccent
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Tarjeta de Avance Académico
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("AVANCE ACADÉMICO", fontSize = 12.sp, color = GreenCarabinerosLight, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Segundo semestre", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("74%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = GreenCarabineros)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.74f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GoldAccent,
                            trackColor = Color.LightGray.copy(alpha = 0.4f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta de Descargas (Offline)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateBottomBar("downloads")
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenCarabinerosLight)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Descargas", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("14 archivos disponibles sin conexión", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                        }
                        Badge(containerColor = GoldAccent, contentColor = Color.Black) {
                            Text("14", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("TU FORMACIÓN · Ramos activos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GreenCarabineros)
                Spacer(modifier = Modifier.height(12.dp))

                // Ramo 1: Derecho Procesal Penal
                SubjectCard(
                    code = "DPP-204",
                    title = "Derecho Procesal Penal",
                    teacher = "My. Juan Pérez · 4 unidades",
                    progress = 0.62f,
                    onClick = { onNavigateToSubject("DPP-204") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ramo 2: Ética y Doctrina Institucional
                SubjectCard(
                    code = "ETI-110",
                    title = "Ética y Doctrina Institucional",
                    teacher = "Cap. María Soto · 3 unidades",
                    progress = 0.88f,
                    onClick = { onNavigateToSubject("ETI-110") }
                )
            }
        }
    }
}

@Composable
fun SubjectCard(
    code: String,
    title: String,
    teacher: String,
    progress: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(code, fontSize = 12.sp, color = GreenCarabinerosLight, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(teacher, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = GreenCarabineros,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
        }
    }
}