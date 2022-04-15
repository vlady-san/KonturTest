package com.example.konturtest.data

data class ContactsResp (
    val id: String,
    val name: String,
    val phone: String,
    val height: Double,
    val biography: String,
    val temperament: String,
    val educationPeriod: EducationPeriodResp
)

data class EducationPeriodResp (
    val start: String,
    val end: String
)