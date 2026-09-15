package com.flatcode.littlenote.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.data.dao.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}