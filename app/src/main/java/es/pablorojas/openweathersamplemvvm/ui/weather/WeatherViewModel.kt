package es.pablorojas.openweathersamplemvvm.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.pablorojas.openweathersamplemvvm.data.models.LocationData
import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeather
import es.pablorojas.openweathersamplemvvm.data.repository.local.LocationProvider
import es.pablorojas.openweathersamplemvvm.data.repository.remote.WeatherRepository
import es.pablorojas.openweathersamplemvvm.utils.NetworkUtils
import es.pablorojas.openweathersamplemvvm.utils.RequestCompleteListener
import es.pablorojas.openweathersamplemvvm.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val networkUtil: NetworkUtils
) : ViewModel() {

    val locationLiveData = MutableLiveData<LocationData>()
    val locationLiveDataFailure = MutableLiveData<String>()

    val weatherByLocation =  MutableLiveData<Resource<ResponseWeather>>()

    fun getCurrentLocation(model: LocationProvider) {
        model.getUserCurrentLocation(object : RequestCompleteListener<LocationData> {
            override fun onRequestCompleted(data: LocationData) {
                locationLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String?) {
                locationLiveDataFailure.postValue(errorMessage.toString())
            }
        })
    }

    fun getFakeCurrentLocation() {
        locationLiveData.postValue(LocationData(40.416775, -3.703790))
    }

    fun getWeatherByLocation(latitude: String, longitude: String) =
        viewModelScope.launch { safeGetWeatherByLocation(latitude, longitude) }


    private suspend fun safeGetWeatherByLocation(latitude: String, longitude: String) {
        weatherByLocation.postValue(Resource.Loading())
        try {
            if (networkUtil.hasInternetConnection()) {
                val response = weatherRepository.getWeatherByLocation(latitude, longitude)
                weatherByLocation.postValue(handleWeatherByLocationResponse(response))
            } else {
                weatherByLocation.postValue(
                    Resource.Error(
                        "NO_INTERNET_CONNECTION"
                    )
                )
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> weatherByLocation.postValue(
                    Resource.Error(
                        "NETWORK_FAILURE"
                    )
                )
                else ->
                    weatherByLocation.postValue(
                        Resource.Error(
                            "CONVERSION_FAILURE"
                        )
                    )
            }
        }
    }

    private fun handleWeatherByLocationResponse(response: Response<ResponseWeather>): Resource<ResponseWeather> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
