package es.pablorojas.openweathersamplemvvm.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

fun showToast(context: Context, message: String, length: Int) {
    Toast.makeText(context, message, length).show()
}

fun showSnackbar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        BaseTransientBottomBar.LENGTH_SHORT
    ).show()
}

fun roundOneDecimal(number: Double): Double {
    return number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}

fun Int.unixTimestampToTimeString(): String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}