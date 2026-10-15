package com.example.mystudentapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val studentId: Int = 0,
    val name: String,
    val course: String,
    val year: Int
)
