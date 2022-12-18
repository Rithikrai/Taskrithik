package com.rithik.task.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Topwear(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int,
    val path: String
)