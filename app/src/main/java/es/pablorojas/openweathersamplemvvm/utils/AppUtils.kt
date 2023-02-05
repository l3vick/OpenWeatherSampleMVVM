package es.pablorojas.openweathersamplemvvm.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.data.models.WeatherIconCode
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

fun getNameDayFromTimestamp(timeStamp: Long): String {
    val date = Date(timeStamp * 1000)
    val outFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return outFormat.format(date).take(3)
}

fun getDrawableResID(icon: String): Int {
    when (icon) {
        WeatherIconCode.W01d.iconCode -> return R.drawable.w01d
        WeatherIconCode.W01n.iconCode -> return R.drawable.w01n
        WeatherIconCode.W02d.iconCode -> return R.drawable.w02d
        WeatherIconCode.W02n.iconCode -> return R.drawable.w02n
        WeatherIconCode.W03d.iconCode -> return R.drawable.w02d
        WeatherIconCode.W03n.iconCode -> return R.drawable.w02n
        WeatherIconCode.W04d.iconCode -> return R.drawable.w04
        WeatherIconCode.W04n.iconCode -> return R.drawable.w04
        WeatherIconCode.W09d.iconCode -> return R.drawable.w09
        WeatherIconCode.W09n.iconCode -> return R.drawable.w09
        WeatherIconCode.W10d.iconCode -> return R.drawable.w10d
        WeatherIconCode.W10n.iconCode -> return R.drawable.w10n
        WeatherIconCode.W11d.iconCode -> return R.drawable.w11
        WeatherIconCode.W11n.iconCode -> return R.drawable.w11
        WeatherIconCode.W13d.iconCode -> return R.drawable.w13
        WeatherIconCode.W13n.iconCode -> return R.drawable.w13
        WeatherIconCode.W50d.iconCode -> return R.drawable.w50
        WeatherIconCode.W50n.iconCode -> return R.drawable.w50
    }
    return R.drawable.n_a
}