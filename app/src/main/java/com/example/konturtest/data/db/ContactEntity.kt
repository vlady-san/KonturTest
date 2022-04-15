package com.example.konturtest.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val phone: String,
    val height: Double,
    val biography: String,
    val temperament: String,
    val educationStart: String,
    val educationEnd: String
)