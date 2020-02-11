package com.victor.nuvem


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.victor.nuvem.dao.TaskDao
import com.victor.nuvem.entities.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE : TaskRoomDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : TaskRoomDatabase {

            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "database.db").build()
            }
            return INSTANCE as TaskRoomDatabase
        }
    }
}