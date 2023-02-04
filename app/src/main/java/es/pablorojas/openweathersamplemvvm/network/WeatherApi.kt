package es.pablorojas.openweathersamplemvvm.network

import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeather
import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherByLocation(
        @Query("lat")
        latitude:String,
        @Query("lon")
        longitude:String
    ): Response<ResponseWeather>

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lat")
        latitude:String,
        @Query("lon")
        longitude:String,
        @Query("exclude")
        exclude:String
    ): Response<ResponseWeatherForecast>
}