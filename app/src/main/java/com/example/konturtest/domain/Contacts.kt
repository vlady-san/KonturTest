package com.example.konturtest.domain

import com.example.konturtest.DateUtils
import java.io.Serializable
import java.util.*

data class Contacts (
    val id: String,
    val name: String,
    val phone: String,
    val height: Double,
    val biography: String,
    private val _temperament: String,
    val educationPeriod: EducationPeriod
) : Serializable{
    //используется для автоматического написания слова с большой буквы
    val temperament = _temperament.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

data class EducationPeriod (
    val start: String,
    val end: String
) : Serializable

//для удобного вывода периода обучения на экране детальной информации контакта
fun EducationPeriod.formPeriod(): String {
    return "${DateUtils.formPrettyDate(this.start)} - ${DateUtils.formPrettyDate(this.end)}"
}