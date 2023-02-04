package es.pablorojas.openweathersamplemvvm.utils

interface RequestCompleteListener<T> {
    fun onRequestCompleted(data:T)
    fun onRequestFailed(errorMessage:String?)
}