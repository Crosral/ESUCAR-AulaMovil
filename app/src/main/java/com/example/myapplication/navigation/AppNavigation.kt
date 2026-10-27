package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.UserEntity
import com.example.myapplication.ui.screens.*
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 1. AUTENTICACIÓN
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "DOCENTE") {
                        navController.navigate("teacher_panel") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onRegisterUser = { username, pass, name, role, onResult ->
                    scope.launch {
                        try {
                            val newUser = UserEntity(
                                username = username,
                                passwordHash = pass,
                                fullName = name,
                                role = role
                            )
                            userDao.registerUser(newUser)
                            onResult(true)
                        } catch (e: Exception) {
                            onResult(false)
                        }
                    }
                },
                onVerifyUser = { username, pass, onResult ->
                    scope.launch {
                        val user = userDao.login(username, pass)
                        if (user != null) {
                            onResult(UserRole(user.role))
                        } else {
                            onResult(null)
                        }
                    }
                }
            )
        }

        // 2. DASHBOARD ESTUDIANTE
        composable("dashboard") {
            DashboardScreen(
                onNavigateToSubject = { subjectCode ->
                    navController.navigate("subject_units")
                },
                onNavigateToNotifications = {
                    navController.navigate("notifications")
                },
                onNavigateBottomBar = { route ->
                    val cleanRoute = route.lowercase()
                    when {
                        cleanRoute.contains("download") || cleanRoute.contains("descarga") || cleanRoute.contains("resource") -> {
                            navController.navigate("downloads")
                        }
                        cleanRoute.contains("ramo") || cleanRoute.contains("unit") || cleanRoute.contains("subject") -> {
                            navController.navigate("subject_units")
                        }
                        cleanRoute.contains("consulta") || cleanRoute.contains("inbox") || cleanRoute.contains("query") -> {
                            navController.navigate("query_inbox")
                        }
                        cleanRoute.contains("perfil") || cleanRoute.contains("profile") -> {
                            navController.navigate("profile")
                        }
                        cleanRoute == "dashboard" -> {
                            // Permanece en Dashboard
                        }
                        else -> {
                            navController.navigate(route)
                        }
                    }
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 3. PERFIL ESTUDIANTE
        composable("profile") {
            ProfileScreen(
                onNavigateBottomBar = { route ->
                    val cleanRoute = route.lowercase()
                    when {
                        cleanRoute == "dashboard" -> navController.navigate("dashboard")
                        cleanRoute.contains("ramo") || cleanRoute.contains("subject") -> navController.navigate("subject_units")
                        cleanRoute.contains("download") || cleanRoute.contains("descarga") -> navController.navigate("downloads")
                        cleanRoute.contains("consulta") || cleanRoute.contains("query") -> navController.navigate("query_inbox")
                        cleanRoute.contains("perfil") || cleanRoute.contains("profile") -> { }
                        else -> navController.navigate(route)
                    }
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 4. RAMOS / UNIDADES
        composable("subject_units") {
            SubjectUnitsScreen(
                subjectCode = "DPP-204",
                onNavigateBack = { navController.popBackStack() },
                onSelectUnit = { navController.navigate("downloads") }
            )
        }

        // 5. DESCARGAS / RECURSOS
        composable("downloads") {
            ResourceListScreen(
                onSelectResource = { resourceId ->
                    navController.navigate("resource_viewer/$resourceId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("resource_viewer/{resourceId}") {
            ResourceViewerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. PANEL DOCENTE (Acceso exclusivo por Login como DOCENTE)
        composable("teacher_panel") {
            TeacherPanelScreen(
                onNavigateBack = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("query_inbox") {
            QueryInboxScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("notifications") {
            NotificationsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}