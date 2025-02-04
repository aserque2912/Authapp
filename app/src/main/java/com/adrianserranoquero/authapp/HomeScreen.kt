package com.adrianserranoquero.authapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val userEmail = FirebaseAuthHelper.getCurrentUser()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bienvenido: $userEmail")
        Button(onClick = {
            FirebaseAuthHelper.signOut()
            navController.navigate("login")
        }) {
            Text("Cerrar Sesión")
        }
    }
}
