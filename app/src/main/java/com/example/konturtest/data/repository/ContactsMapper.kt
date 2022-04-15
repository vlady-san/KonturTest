package com.example.konturtest.data.repository

import com.example.konturtest.data.ContactsResp
import com.example.konturtest.data.db.ContactEntity
import com.example.konturtest.domain.Contacts
import com.example.konturtest.domain.EducationPeriod

class ContactsMapper {

    fun toContacts(resp: ContactsResp) : Contacts {
        return Contacts(
            resp.id,
            resp.name,
            resp.phone,
            resp.height,
            resp.biography,
            resp.temperament,
            EducationPeriod(
                resp.educationPeriod.start,
                resp.educationPeriod.end
            )
        )
    }

    fun toContacts(entity: ContactEntity) : Contacts {
        return Contacts(
            entity.id,
            entity.name,
            entity.phone,
            entity.height,
            entity.biography,
            entity.temperament,
            EducationPeriod(
                entity.educationStart,
                entity.educationEnd
            )
        )
    }

    fun toContactEntity(contacts: Contacts) : ContactEntity {
        return ContactEntity(
            contacts.id,
            contacts.name,
            contacts.phone,
            contacts.height,
            contacts.biography,
            contacts.temperament,
            contacts.educationPeriod.start,
            contacts.educationPeriod.end
        )
    }
}