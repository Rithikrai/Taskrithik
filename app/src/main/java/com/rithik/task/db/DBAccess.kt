package com.rithik.task.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DBAccess {
    @Insert
    fun insertTopWear(topwear: Topwear?)

    @Insert
    fun insertBottomWear(bottomwear: Bottomwear?)

    @Insert
    fun insertWishlist(wishlist: Wishlist?)

    @Delete
    fun deleteWishlist(wishlist: Wishlist?)

    @Query("Select * from Topwear order by id desc")
//    fun getTopWear(): LiveData<List<Topwear?>?>?
    fun getTopWear(): List<Topwear?>?

    @Query("Select * from Bottomwear order by id desc")
//    fun getBottomWear(): LiveData<List<Bottomwear?>?>?
    fun getBottomWear(): List<Bottomwear?>?

    @Query("Select * from Wishlist where topId = :tid and bottomId = :bid")
    fun getWishData(tid: Int, bid: Int): Wishlist?
}