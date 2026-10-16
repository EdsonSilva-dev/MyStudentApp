
package com.example.mystudentapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {

    @Insert
    suspend fun insertStudent(student: Student): Long

    @Query("SELECT * FROM students WHERE studentId = :id LIMIT 1")
    suspend fun findStudentById(id: Int): Student?
}
