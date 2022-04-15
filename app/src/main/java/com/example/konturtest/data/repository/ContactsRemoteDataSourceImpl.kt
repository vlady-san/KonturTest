package com.example.konturtest.data.repository

import android.content.Context
import com.example.konturtest.data.ContactsService
import com.example.konturtest.data.ResultWrapper
import com.example.konturtest.domain.Contacts
import kotlinx.coroutines.*
import android.net.ConnectivityManager

import android.net.NetworkInfo
import com.example.konturtest.data.ContactsResp
import retrofit2.Response


class ContactsRemoteDataSourceImpl(
    private val service: ContactsService,
    private val mapper: ContactsMapper
) : ContactsRemoteDataSource {

    override suspend fun getContacts(dispatcher: CoroutineDispatcher): ResultWrapper<List<Contacts>> {
        return withContext(dispatcher){


            //Запускаю паралельно три потока на получение данных и жду от них результата

            var j = async { handleError("generated-01.json", service::getContacts) }
            var j2 = async { handleError("generated-02.json", service::getContacts) }
            var j3 = async { handleError("generated-03.json", service::getContacts) }

            var res1 = j.await()
            var res2 = j2.await()
            var res3 = j3.await()

            var contacts = mutableListOf<Contacts>()

            //Если запросы прошли без ошибок и тело запроса корректно, то считаем результат успешным
            if(res1?.isSuccessful == true && res2?.isSuccessful == true && res3?.isSuccessful == true)
            {
                contacts.addAll(res1.body()?.map { mapper.toContacts(it)} ?: emptyList())
                contacts.addAll(res2.body()?.map { mapper.toContacts(it)} ?: emptyList())
                contacts.addAll(res3.body()?.map { mapper.toContacts(it)} ?: emptyList())

                ResultWrapper.Success(contacts)
            }
            else ResultWrapper.Error

        }
    }

    /*
        Очень простой вариант обработки ошибок, которые не связаны с ошибками сервера
        Для больших проектов удобнее будет написать interceptor
     */
    private suspend fun handleError (source : String, func : suspend (source : String) -> Response<List<ContactsResp>>) : Response<List<ContactsResp>>?{
        return try{
            func.invoke(source)
        }
        catch (e: Exception) {
            null
        }
    }

}