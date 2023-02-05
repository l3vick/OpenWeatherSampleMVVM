package es.pablorojas.openweathersamplemvvm.ui.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeatherForecast
import es.pablorojas.openweathersamplemvvm.data.repository.remote.WeatherRepository
import es.pablorojas.openweathersamplemvvm.utils.NetworkUtils
import es.pablorojas.openweathersamplemvvm.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val networkUtil: NetworkUtils
) : ViewModel() {

    val weatherForecast = MutableLiveData<Resource<ResponseWeatherForecast>>()

    fun getWeatherForecast(latitude: String, longitude: String, exclude: String) =
        viewModelScope.launch { safeGetWeatherByLocation(latitude, longitude, exclude) }


    private suspend fun safeGetWeatherByLocation(
        latitude: String,
        longitude: String,
        exclude: String
    ) {
        weatherForecast.postValue(Resource.Loading())
        try {
            if (networkUtil.hasInternetConnection()) {
                val response = weatherRepository.getWeatherForecast(latitude, longitude, exclude)
                weatherForecast.postValue(handleWeatherForecastResponse(response))
            } else {
                weatherForecast.postValue(
                    Resource.Error(
                        "NO_INTERNET_CONNECTION"
                    )
                )
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> weatherForecast.postValue(
                    Resource.Error(
                        "NETWORK_FAILURE"
                    )
                )
                else ->
                    weatherForecast.postValue(
                        Resource.Error(
                            "CONVERSION_FAILURE"
                        )
                    )
            }
        }
    }

    private fun handleWeatherForecastResponse(response: Response<ResponseWeatherForecast>): Resource<ResponseWeatherForecast> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}