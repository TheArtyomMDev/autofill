package com.example.testautofillapp

import android.app.assist.AssistStructure
import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun isEmail(child: AssistStructure.ViewNode): Boolean {
    if(
        child.idEntry?.contains("username") == true ||
        child.autofillHints?.get(0)?.contains("username") == true ||
        child.hint?.contains("username") == true
    )
        return true

    if(child.htmlInfo != null && child.htmlInfo?.attributes != null) {
        for (pair in child.htmlInfo!!.attributes!!) {
            // try так как pair.first или pair.second могут равняться null
            try {
                val first = pair.first.lowercase(Locale.getDefault())
                val second = pair.second.lowercase(Locale.getDefault())
                val firstList = listOf("name", "id", "type")
                val secondList = listOf("login", "username", "email")

                if(first in firstList && second in secondList) return true
            } catch (e: Exception) {}
        }
    }


    return false
}

@RequiresApi(Build.VERSION_CODES.O)
fun isPassword(child: AssistStructure.ViewNode): Boolean {
    if(
        child.idEntry?.contains("password") == true ||
        child.autofillHints?.get(0)?.contains("password") == true ||
        child.hint?.contains("password") == true
    )
        return true

    if(child.htmlInfo != null && child.htmlInfo?.attributes != null) {
        for (pair in child.htmlInfo!!.attributes!!) {
            // try так как pair.first или pair.second могут равняться null
            try {
                val first = pair.first.lowercase(Locale.getDefault())
                val second = pair.second.lowercase(Locale.getDefault())
                val firstList = listOf("name", "id", "type")
                val secondList = listOf("password", "pwd")

                if(first in firstList && second in secondList) return true
            } catch (e: Exception) {}
        }
    }
    return false
}