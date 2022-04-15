package com.example.konturtest.data.repository

import com.example.konturtest.data.ResultWrapper
import com.example.konturtest.domain.Contacts
import com.example.konturtest.domain.ContactsRepository
import kotlinx.coroutines.CoroutineDispatcher

class ContactsRepositoryImpl(
    private var remoteDataSource: ContactsRemoteDataSource,
    private var localDataSource: ContactsLocalDataSource
) : ContactsRepository {

    //При успешных запросах обновляем бд
    override suspend fun loadContacts(dispatcher: CoroutineDispatcher): ResultWrapper<List<Contacts>> {
        var res = remoteDataSource.getContacts(dispatcher)
        if (res is ResultWrapper.Success)
        {
            res.value?.let { localDataSource.insertContacts(it) }
        }
        return res
    }

    override suspend fun getContacts(): List<Contacts> {
        return localDataSource.getContacts()
    }
}