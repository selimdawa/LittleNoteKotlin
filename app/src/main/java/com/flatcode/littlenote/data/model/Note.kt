package com.flatcode.littlenote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var remoteId: String? = null,
    var title: String? = null,
    var content: String? = null
)