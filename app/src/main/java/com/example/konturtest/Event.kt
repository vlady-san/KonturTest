package com.example.konturtest

//Объект разрешающий получать данные один раз. Нужен для отображения диалогов, тостов, снекбаров,
//чтобы они не появлялись на экране снова при возвращении из бэкграунда или с другого экрана
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}