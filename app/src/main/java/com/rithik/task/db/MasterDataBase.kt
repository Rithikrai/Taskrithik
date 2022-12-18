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

    companion object {
        private var apparelInstance: MasterDataBase? = null
        @Synchronized
        fun getInstance(context: Context): MasterDataBase? {
            if (apparelInstance == null) {
                apparelInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MasterDataBase::class.java,
                    "Rithik"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return apparelInstance
        }
    }
}