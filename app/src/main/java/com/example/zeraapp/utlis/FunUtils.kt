package com.example.zeraapp.utlis

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun setLocale(activity: Activity, languageCode: String?, update: Boolean) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = activity.resources
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
    if (update)
        activity.recreate()
}

fun getEtText(editText: EditText): String {
    return editText.text.toString().trim()
}

fun
        setLog(tagTitle: String, tagMessage: String) {
    Log.v(tagTitle, tagMessage)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun moneyCredited(message: String): String {
    val dec = DecimalFormat("##,###.00")
    val money = dec.format(message.toDouble())

    return "+$" + money
}

fun moneyWidthdraw(message: String): String {
    val dec = DecimalFormat("##,###.00")
    val money = dec.format(message.toDouble())
    return "-$" + money
}

fun makeDollars(message: String): String {
    val dec = DecimalFormat("#,###.00")
    val money = dec.format(message.toDouble())
    return "$" + money
}

fun showHashIds(message: String): String {
    return "#" + message
}

fun addMonths(month: Int, pattern: String): String {
    try {
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.MONTH, month)
        val date = cal.time
        val format1 = SimpleDateFormat(pattern)
//    val date1: String = format1.format(date)
        return format1.format(date).toString()
    } catch (e: java.lang.Exception) {
        return ""
    }

}

fun displayDate(date: String): String {
    try {
        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date: Date = originalFormat.parse(date)
        val formattedDate: String = targetFormat.format(date)
        return formattedDate
    } catch (e: java.lang.Exception) {
        return ""
    }
}

fun convertDate(originalDate: String, patternSource: String, newPattern: String): String {
    try {
        val originalFormat: DateFormat = SimpleDateFormat(patternSource, Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat(newPattern)
        val date: Date = originalFormat.parse(originalDate)
        val formattedDate: String = targetFormat.format(date)
        return formattedDate
    } catch (e: java.lang.Exception) {
        return ""
    }
}

fun roundOffDouble(word: String): String {
    val number2digits: Double = String.format("%.2f", word.toDouble()).toDouble()
    return number2digits.toString()
}

fun extractWord(word: String): String {
    if (word != null) {
        if (word.contains(" "))
            return word.substring(0, word.indexOf(" "))
        else
            return word
    }
    return ""
}

fun replaceWord(word: String, sentence: String, newWord: String): String {
    if (sentence.contains(word, ignoreCase = true))
        return sentence.replace(word, newWord, ignoreCase = true)
    return sentence
}


