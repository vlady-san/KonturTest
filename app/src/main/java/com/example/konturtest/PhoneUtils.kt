package com.example.konturtest

import java.util.regex.Matcher
import java.util.regex.Pattern

class PhoneUtils {
    companion object{
        fun getOnlyNumbersOfString(phone : String): String {
            val REGEX = "[^0-9]+"
            val p: Pattern = Pattern.compile(REGEX)
            val m: Matcher = p.matcher(phone)
            return m.replaceAll("")
        }
    }
}