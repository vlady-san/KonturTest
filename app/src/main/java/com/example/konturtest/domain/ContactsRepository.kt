package com.example.konturtest.domain

import com.example.konturtest.data.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ContactsRepository {
    suspend fun loadContacts(dispatcher: CoroutineDispatcher) : ResultWrapper<List<Contacts>>
    suspend fun getContacts() : List<Contacts>
}