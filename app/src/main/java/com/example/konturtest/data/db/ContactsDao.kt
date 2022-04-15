package com.example.konturtest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(elements: List<ContactEntity>)

    @Query("SELECT * FROM contactentity where id = :id")
    fun getContact(id : Int): ContactEntity

    @Query("SELECT * FROM contactentity")
    fun getContacts(): List<ContactEntity>

    @Query("DELETE FROM contactentity")
    fun deleteContacts()
}