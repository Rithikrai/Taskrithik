package com.rithik.task.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//class MasterDataBase {
//}
@Database(
    entities = [Topwear::class, Bottomwear::class, Wishlist::class],
    version = 1,
    exportSchema = false
)
abstract class MasterDataBase : RoomDatabase() {

    abstract fun allDao(): DBAccess

}