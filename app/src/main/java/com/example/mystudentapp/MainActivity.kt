
package com.example.mystudentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MyStudentApp()
            }
        }
    }
}

@Composable
fun MyStudentApp() {
    var currentPage by rememberSaveable {
        mutableStateOf("home")
    }

    BackHandler(enabled = currentPage != "home") {
        currentPage = "home"
    }

    when (currentPage) {
        "home" -> HomeScreen(
            onRegister = { currentPage = "register" },
            onSearch = { currentPage = "search" }
        )

        "register" -> RegistrationScreen(
            onBack = { currentPage = "home" }
        )

        "search" -> SearchScreen(
            onBack = { currentPage = "home" }
        )
    }
}
