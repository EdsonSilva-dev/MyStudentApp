
package com.example.mystudentapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mystudentapp.database.Student
import com.example.mystudentapp.database.StudentDatabase
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val database = remember {
        StudentDatabase.getDatabase(context)
    }
    val studentDao = remember(database) {
        database.studentDao()
    }
    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var course by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = "Student Registration",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = course,
            onValueChange = { course = it },
            label = { Text("Course") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year (1-3)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = !isSaving,
            onClick = {
                val studentYear = year.toIntOrNull()

                when {
                    name.isBlank() || course.isBlank() -> {
                        message = "Please complete all fields."
                    }

                    studentYear == null ||
                            studentYear !in 1..4 -> {
                        message = "Year must be between 1 and 4."
                    }

                    else -> {
                        isSaving = true
                        message = ""

                        scope.launch {
                            try {
                                val student = Student(
                                    name = name.trim(),
                                    course = course.trim(),
                                    year = studentYear
                                )

                                val newId =
                                    studentDao.insertStudent(student)

                                message =
                                    "Student registered! ID: $newId"

                                name = ""
                                course = ""
                                year = ""

                            } catch (e: Exception) {
                                message =
                                    "Error saving student."
                            } finally {
                                isSaving = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register Student")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(message)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}
