
package com.example.mystudentapp

import androidx.compose.foundation.layout.*
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
fun SearchScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val database = remember {
        StudentDatabase.getDatabase(context)
    }
    val studentDao = remember(database) {
        database.studentDao()
    }
    val scope = rememberCoroutineScope()

    var studentId by rememberSaveable {
        mutableStateOf("")
    }

    var message by rememberSaveable {
        mutableStateOf("")
    }

    var foundStudent by remember {
        mutableStateOf<Student?>(null)
    }

    var isSearching by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Find Student",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = studentId,
            onValueChange = {
                studentId = it
                foundStudent = null
                message = ""
            },
            label = { Text("Student ID") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = !isSearching,
            onClick = {
                val id = studentId.toIntOrNull()

                foundStudent = null

                if (id == null || id <= 0) {
                    message = "Please enter a valid Student ID."
                } else {
                    isSearching = true
                    message = ""

                    scope.launch {
                        try {
                            val student =
                                studentDao.findStudentById(id)

                            if (student != null) {
                                foundStudent = student
                            } else {
                                message = "Student not found."
                            }

                        } catch (e: Exception) {
                            message = "Error searching student."
                        } finally {
                            isSearching = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Find Student")
        }

        Spacer(modifier = Modifier.height(16.dp))

        foundStudent?.let { student ->
            Text("Name: ${student.name}")
            Text("Course: ${student.course}")
        }

        if (message.isNotEmpty()) {
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
