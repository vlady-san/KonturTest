package com.example.konturtest.data.repository

import com.example.konturtest.data.ResultWrapper
import com.example.konturtest.domain.Contacts
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ContactsRemoteDataSource {
    suspend fun getContacts(dispatcher: CoroutineDispatcher): ResultWrapper<List<Contacts>>
}