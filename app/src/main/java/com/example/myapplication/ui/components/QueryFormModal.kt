package com.example.myapplication.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.GreenCarabineros
import com.example.myapplication.ui.viewmodel.QueryViewModel

@Composable
fun QueryFormModal(
    subjectName: String,
    unitName: String,
    resourceTitle: String,
    viewModel: QueryViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current // Obtenemos el contexto para el Toast
    var asunto by remember { mutableStateOf("") }
    var detalle by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Consulta al docente") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Asignatura: $subjectName", style = MaterialTheme.typography.bodySmall)
                Text(text = "Unidad: $unitName", style = MaterialTheme.typography.bodySmall)

                OutlinedTextField(
                    value = asunto,
                    onValueChange = { asunto = it },
                    label = { Text("Asunto") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = detalle,
                    onValueChange = { detalle = it },
                    label = { Text("Describe tu duda...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = GreenCarabineros),
                enabled = asunto.isNotBlank() && detalle.isNotBlank(),
                onClick = {
                    // 1. Guarda en la BD mediante el ViewModel
                    viewModel.sendQuery(
                        subject = subjectName,
                        unit = unitName,
                        resource = resourceTitle,
                        title = asunto,
                        detail = detalle
                    )

                    // 2. Muestra el mensaje exigido por la pauta de evaluación
                    Toast.makeText(
                        context,
                        "Para una futura actualización",
                        Toast.LENGTH_SHORT
                    ).show()

                    onDismiss()
                }
            ) {
                Text("Enviar al profesor")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}