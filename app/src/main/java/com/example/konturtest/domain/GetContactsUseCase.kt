package com.example.konturtest.domain

import com.example.konturtest.data.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetContactsUseCase(private val repository: ContactsRepository) {
    //загрузка контактов с сервера и обновление бд
    suspend fun loadContacts(dispatcher: CoroutineDispatcher = Dispatchers.IO): ResultWrapper<List<Contacts>> {
        return repository.loadContacts(dispatcher)
    }

    //получение контактов из бд
    suspend fun getContacts(): List<Contacts> {
        return repository.getContacts()
    }
}