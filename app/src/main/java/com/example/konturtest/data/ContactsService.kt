package com.example.konturtest.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ContactsService {

    @GET("{source}")
    suspend fun getContacts(@Path("source") source : String) : Response<List<ContactsResp>>

}