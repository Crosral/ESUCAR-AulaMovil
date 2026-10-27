package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.NativeUtils

@Composable
fun LoginScreen(
    onLoginSuccess: (role: String) -> Unit,
    onRegisterUser: (username: String, pass: String, name: String, role: String, onResult: (Boolean) -> Unit) -> Unit,
    onVerifyUser: (username: String, pass: String, onResult: (UserRole?) -> Unit) -> Unit
) {
    val context = LocalContext.current
    var isRegisterMode by remember { mutableStateOf(false) }

    // Campos del formulario
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("ESTUDIANTE") }

    // Estados de Error Individuales (IE 2.1.2)
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var fullNameError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegisterMode) "Registro de Usuario" else "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Nombre Completo (Solo en modo registro)
        if (isRegisterMode) {
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError = it.isBlank()
                },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                isError = fullNameError,
                supportingText = {
                    if (fullNameError) {
                        Text("El nombre es obligatorio", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Campo Usuario / Correo
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = it.isBlank()
            },
            label = { Text("Usuario / Correo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = usernameError,
            supportingText = {
                if (usernameError) {
                    Text("Ingresa tu usuario o correo", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.isBlank()
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError,
            supportingText = {
                if (passwordError) {
                    Text("La contraseña es requerida", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Selección de Rol
        if (isRegisterMode) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Selecciona Tipo de Usuario:", style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedRole == "ESTUDIANTE",
                    onClick = { selectedRole = "ESTUDIANTE" }
                )
                Text("Estudiante")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = selectedRole == "DOCENTE",
                    onClick = { selectedRole = "DOCENTE" }
                )
                Text("Docente")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Validación visual de campos obligatorios
                usernameError = username.isBlank()
                passwordError = password.isBlank()
                if (isRegisterMode) fullNameError = fullName.isBlank()

                if (usernameError || passwordError || (isRegisterMode && fullNameError)) {
                    errorMessage = "Por favor completa todos los campos requeridos"
                    return@Button
                }

                // Uso de Recurso Nativo: Verificación de estado de red (IE 2.4.1)
                if (!NativeUtils.isNetworkAvailable(context)) {
                    Toast.makeText(context, "Aviso: Operando en modo Offline", Toast.LENGTH_SHORT).show()
                }

                if (isRegisterMode) {
                    onRegisterUser(username, password, fullName, selectedRole) { success ->
                        if (success) {
                            errorMessage = ""
                            onLoginSuccess(selectedRole)
                        } else {
                            errorMessage = "El usuario ya se encuentra registrado"
                        }
                    }
                } else {
                    onVerifyUser(username, password) { userFound ->
                        if (userFound != null) {
                            errorMessage = ""
                            onLoginSuccess(userFound.role)
                        } else {
                            errorMessage = "Usuario o contraseña incorrectos"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegisterMode) "Registrar e Ingresar" else "Ingresar")
        }

        TextButton(onClick = {
            isRegisterMode = !isRegisterMode
            errorMessage = ""
            usernameError = false
            passwordError = false
            fullNameError = false
        }) {
            Text(
                if (isRegisterMode) "¿Ya tienes cuenta? Inicia Sesión"
                else "¿No tienes cuenta? Regístrate aquí"
            )
        }
    }
}

data class UserRole(val role: String)