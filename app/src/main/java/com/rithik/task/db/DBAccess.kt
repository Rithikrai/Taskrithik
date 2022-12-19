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

    @Query("DELETE FROM Wishlist WHERE id = :id AND topId = :tid and bottomId = :bid")
    fun deleteRecord(id: Int, tid: Int, bid: Int)

    @Query("Select * from Topwear order by id desc")
    fun getTopWear(): List<Topwear?>?

    @Query("Select * from Bottomwear order by id desc")
    fun getBottomWear(): List<Bottomwear?>?

    @Query("Select * from Wishlist where topId = :tid and bottomId = :bid")
    fun getWishDataList(tid: Int, bid: Int): List<Wishlist?>?

    @Query("Select * from Wishlist where topId = :tid and bottomId = :bid")
    fun getWishData(tid: Int, bid: Int): Wishlist?
}