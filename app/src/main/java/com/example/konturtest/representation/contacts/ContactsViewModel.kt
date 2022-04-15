package com.example.konturtest.representation.contacts

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.konturtest.AppDatabase
import com.example.konturtest.Event
import com.example.konturtest.LastRequestSharedPreference
import com.example.konturtest.data.ContactsService
import com.example.konturtest.data.ResultWrapper
import com.example.konturtest.data.ServiceGenerator
import com.example.konturtest.data.repository.ContactsLocalDataSourceImpl
import com.example.konturtest.data.repository.ContactsMapper
import com.example.konturtest.data.repository.ContactsRemoteDataSourceImpl
import com.example.konturtest.data.repository.ContactsRepositoryImpl
import com.example.konturtest.domain.Contacts
import com.example.konturtest.domain.ContactsRepository
import com.example.konturtest.domain.GetContactsUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private var _contacts = MutableLiveData<List<Contacts>>()
    var contacts : LiveData<List<Contacts>> = _contacts

    private var _dateIsLoading = MutableLiveData<Boolean>()
    var dateIsLoading : LiveData<Boolean> = _dateIsLoading

    private var _error = MutableLiveData<Event<Boolean>>()
    var error : LiveData<Event<Boolean>> = _error

    private var timeSinceLastRequest : Long = Long.MAX_VALUE

    init {
        getTimeSinceLastRequest(application.baseContext)
    }

    private var repository : ContactsRepository = ContactsRepositoryImpl(
        ContactsRemoteDataSourceImpl(
            ServiceGenerator.createService(ContactsService::class.java),
            ContactsMapper()
        ),
        ContactsLocalDataSourceImpl(
            AppDatabase.getAppDataBase(application.baseContext)?.userDao(),
            ContactsMapper()
        )
    )
    private val useCase = GetContactsUseCase(repository)

    /*
     Сначала запрашиваю данные из бд. Если пусто, значит мы зашли в первый раз.
     Показываем прогресс бар и загружаем контакты с сервера
     Если локальная база не пустая, то отображаем данные и проверяем нужно ли данные обновить
     */
    fun getContacts(){
            viewModelScope.launch(Dispatchers.IO) {

                var contacts = useCase.getContacts()

                if(contacts.isEmpty()) {
                    _dateIsLoading.postValue(true)
                    var isDone = loadContacts()
                    isDone.await()
                    _dateIsLoading.postValue(false)
                }
                else {
                    _contacts.postValue(repository.getContacts())
                    updateIfNeed(timeSinceLastRequest)
                }

            }
        }

    //функция проверяет время, прошедшее с последнего запроса и если оно больше минуты,
    // то обновляем данные
    private fun updateIfNeed(timeSinceLastRequest : Long){
        if(timeSinceLastRequest > 60000) {
            loadContacts()
        }
    }

    //функция запрашивает данные с сервера
    fun loadContacts(): Deferred<Boolean> {
        return viewModelScope.async(Dispatchers.IO) {
            var contacts = useCase.loadContacts()
            when(contacts){
                is ResultWrapper.Success -> {
                    var res = repository.getContacts()
                    _contacts.postValue(res)
                }
                is ResultWrapper.Error -> _error.postValue(Event(true))
            }
            true
        }

    }

    //функция обновляет время последнего запроса. Значение хранится в shared preferences
    fun updateRequestLastTime(context: Context){
        LastRequestSharedPreference.setTimeByRequest(context,
            LastRequestSharedPreference.getContactsKey,
            System.currentTimeMillis())
    }

    //функция получает время последнего запроса и высчитывает разницу с текущим временем
    private fun getTimeSinceLastRequest(context: Context) {
        val lastTimeForRequest = LastRequestSharedPreference.getLastTimeForRequest(context, LastRequestSharedPreference.getContactsKey)
        timeSinceLastRequest = System.currentTimeMillis() - lastTimeForRequest
    }

}