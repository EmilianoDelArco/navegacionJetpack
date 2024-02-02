package com.example.navegacionjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.example.navegacionjetpack.ui.theme.NavegacionJetpackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavegacionJetpackTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

// Definición de las pantallas
sealed class Pantalla(val ruta: String) {
    object ListaDeTareas : Pantalla("lista_de_tareas")
    object DetallesDeTarea : Pantalla("detalles_de_tarea")
}

// Definición de la clase Tarea para representar un objeto en la aplicación
data class Tare(val titulo: String, val descripcion: String)

// Función principal de la aplicación
@Composable
fun App() {
    // Configuración de la navegación
    val navController = rememberNavController()
    // Lista de tareas de ejemplo
    val listaDeTares = listOf(
        Tare("Comprar leche", "Ir al supermercado y comprar leche"),
        Tare("Hacer ejercicio", "Ir al gimnasio y hacer ejercicio")
    )
    // Definición de la navegación y las pantallas
    NavHost(
        navController = navController,
        startDestination = Pantalla.ListaDeTareas.ruta
    ) {
        composable(Pantalla.ListaDeTareas.ruta) {
            ListaDeTareas(navController,listaDeTares)
        }
        composable("${Pantalla.DetallesDeTarea.ruta}/{tituloTarea}") {backStackEntry ->
            // Obtener el título de la tarea de la ruta
            val tituloTarea = backStackEntry.arguments?.getString("tituloTarea") ?: ""
            DetallesDeTarea(navController,listaDeTares, tituloTarea)
        }
    }
}

// Pantalla de lista de tareas
@Composable
fun ListaDeTareas(navController: NavHostController, listaDeTares: List<Tare>) {


    // Compose UI para mostrar la lista de tareas usando LazyColumn
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(listaDeTares) { tarea ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            // Navegar a la pantalla de detalles y pasar la tarea seleccionada
                            .clickable { navController.navigate("${Pantalla.DetallesDeTarea.ruta}/${tarea.titulo}") }
                    ) {
                        Text(tarea.titulo,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

        }
    }
}

// Pantalla de detalles de tarea
@Composable
fun DetallesDeTarea(navController: NavHostController,listaDeTares: List<Tare>, tituloTarea: String) {

    // Buscar la tarea en la lista según el título
    val tareaSeleccionada = listaDeTares.find { it.titulo == tituloTarea }

    // Compose UI para mostrar los detalles de la tarea seleccionada
    Column {
        tareaSeleccionada?.let {
            // Detalles de la tarea
            Text("Título: ${it.titulo}")
            Text("Descripción: ${it.descripcion}")
        }

        // Botón para volver a la lista de tareas
        Button(onClick = {
            // Navegar de regreso a la lista de tareas al hacer clic en el botón
            navController.navigateUp()
        }) {
            Text("Volver a la lista de tareas")
        }
    }
}


