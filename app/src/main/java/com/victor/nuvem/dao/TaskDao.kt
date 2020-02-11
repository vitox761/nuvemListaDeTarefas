package com.victor.nuvem.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.victor.nuvem.entities.Task

@Dao
interface TaskDao {

    @Query("SELECT * from task_table /*order by complete desc*/")
    fun getAllTasks() : LiveData<List<Task>>

    @Insert
    fun insert(todo : Task)

    @Update
    fun update(todo: Task)

    @Delete
    fun delete(todo: Task)

    @Query("DELETE FROM task_table")
    fun deleteAll()
}