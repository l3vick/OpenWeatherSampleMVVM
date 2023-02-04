package es.pablorojas.openweathersamplemvvm.data.repository.remote

import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeather
import es.pablorojas.openweathersamplemvvm.network.WeatherApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {

    suspend fun getWeatherByLocation(
        latitude: String,
        longitude: String
    ): Response<ResponseWeather> =
        weatherApi.getWeatherByLocation(latitude = latitude, longitude = longitude)
}