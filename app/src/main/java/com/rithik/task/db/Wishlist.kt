package com.rithik.task.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Wishlist(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int,
    val topId: Int,
    val bottomId: Int
)