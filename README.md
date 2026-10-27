# ESUCAR - Aula Móvil

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Evaluación:** Parcial 2 - Encargo  
**Institución:** Duoc UC  

# Integrantes del Equipo
* Jeral Melillanca Chávez

---

## Descripción del Proyecto
ESUCAR - Aula Móvil es una solución móvil desarrollada en Jetpack Compose para la Escuela de Suboficiales de Carabineros de Chile. La aplicación permite descentralizar el aprendizaje académico, brindando acceso a recursos educativos sin conexión y estableciendo un canal bidireccional entre alumnos y docentes para la gestión y resolución de consultas académicas.

---

# Funcionalidades e Indicadores de Logro (IE)

## 1. Diseño Visual y Navegación (IE 2.1.1)
* Interfaz institucional basada en Material 3 con paleta de colores corporativa (`GreenCarabineros` y `GoldAccent`).
* Navegación limpia y declarativa mediante `NavHost` y `NavController` (`AppNavigation.kt`).
* Barra de navegación inferior (`BottomNavBar`) sincronizada dinámicamente con las rutas de la app.

## 2. Formularios y Validaciones Visuales (IE 2.1.2)
* Formularios de inicio de sesión, registro de usuarios y creación de consultas.
* Retroalimentación visual inmediata ante errores (`isError = true`, `supportingText` e íconos de advertencia).

## 3. Gestión de Estado y Arquitectura (IE 2.2.1)
* Implementación del patrón de arquitectura MVVM (Model-View-ViewModel).
* Flujos de datos reactivos e inmutables mediante `StateFlow` y `collectAsState`.

## 4. Animaciones de Interacción (IE 2.2.2)
* Animaciones fluidas de entrada y salida con `AnimatedVisibility` para vistas previas de imágenes y banners de error.

## 5. Persistencia Local de Datos (IE 2.3.1)
* Base de datos local SQLite utilizando Room Database.
* Modelado de datos relacional para usuarios (`UserEntity`) y consultas (`QueryEntity`).
* Operaciones asíncronas optimizadas con `suspend` y Kotlin Coroutines.

## 6. Acceso a Recursos Nativos (IE 2.4.1)
* **Recurso Nativo 1 (Cámara):** Captura directa de fotografías adjuntas mediante `ActivityResultContracts.TakePicturePreview` con gestión segura de permisos en tiempo de ejecución (`CAMERA`).
* **Recurso Nativo 2 (Conectividad/Red):** Detección en tiempo real del estado de red del dispositivo vía `ConnectivityManager`.

---

# Tecnologías Utilizadas
* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose (Material 3)
* **Arquitectura:** MVVM
* **Persistencia:** Room / SQLite
* **Compilador/Análisis:** KSP (Kotlin Symbol Processing)
* **Navegación:** Navigation Compose
