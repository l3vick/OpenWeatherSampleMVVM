package es.pablorojas.openweathersamplemvvm.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message:String, length:Int) {
    Toast.makeText(context,message,length).show()
}