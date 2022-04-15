package com.example.konturtest.data.repository

import com.example.konturtest.data.db.ContactsDao
import com.example.konturtest.domain.Contacts


class ContactsLocalDataSourceImpl(
    val dao : ContactsDao?,
    val mapper : ContactsMapper
) : ContactsLocalDataSource {

    override suspend fun insertContacts(contacts: List<Contacts>) {
        dao?.insertContacts(contacts.map { mapper.toContactEntity(it) })
    }

    override suspend fun getContacts(): List<Contacts> {
        return dao?.getContacts()?.map { mapper.toContacts(it) } ?: emptyList()
    }
}