package com.example.konturtest.data.repository

import com.example.konturtest.domain.Contacts

interface ContactsLocalDataSource {
    suspend fun insertContacts(contacts : List<Contacts>)
    suspend fun getContacts() : List<Contacts>
}