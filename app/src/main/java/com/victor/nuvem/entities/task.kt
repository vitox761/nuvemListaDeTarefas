package com.victor.nuvem.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(@PrimaryKey(autoGenerate = true) var tId: Int = 0,
                @ColumnInfo(name = "task_title") var title:String = "",
                @ColumnInfo(name = "complete") var complete : Boolean = false)