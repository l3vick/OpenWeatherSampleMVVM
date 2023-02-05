package es.pablorojas.openweathersamplemvvm.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.core.BaseFragment
import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeather
import es.pablorojas.openweathersamplemvvm.data.models.WeatherIconCode
import es.pablorojas.openweathersamplemvvm.data.repository.local.LocationProvider
import es.pablorojas.openweathersamplemvvm.databinding.FragmentWeatherBinding
import es.pablorojas.openweathersamplemvvm.utils.*


@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding>(
    FragmentWeatherBinding::inflate,
    R.layout.fragment_weather
) {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var locationProvider: LocationProvider

    private var latitude = 0.0f
    private var longitude = 0.0f
    private var city = ""

    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLocation()
    }

    override fun setupView() {

    }

    override fun setupListeners() {
        binding?.incWeatherContent?.forecastBtn?.setOnClickListener {
            val action = WeatherFragmentDirections.actionWeatherFragmentToForecastFragment(
                latitude,
                longitude,
                city
            )
            navController.navigate(action)
        }
    }

    override fun setupObservers() {
        viewModel.locationLiveData.observe(this) {
            latitude = it.latitude?.toFloat() ?: 0.0f
            longitude = it.longitude?.toFloat() ?: 0.0f
            viewModel.getWeatherByLocation(it.latitude.toString(), it.longitude.toString())
        }

        viewModel.weatherByLocation.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.incWeatherContent?.root?.visibility = View.VISIBLE
                        it.data?.let { data -> setupUI(data) }
                    }
                    Status.ERROR -> {
                        showSnackbar(
                            binding?.incWeatherContent?.root as View, it.message.toString()
                        )
                    }
                    Status.LOADING -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    private fun setupUI(data: ResponseWeather) {
        city = data.name
        binding?.incWeatherContent?.weatherIv?.setImageResource(getDrawableResID(data.weather[0].icon))
        binding?.incWeatherContent?.cityTv?.text =
            getString(R.string.city_tv, data.name, data.sys.country)
        binding?.incWeatherContent?.tempTv?.text =
            getString(R.string.temp_tv, roundOneDecimal(data.main.temp).toString())
        binding?.incWeatherContent?.highTempTv?.text =
            getString(R.string.low_high_temp_tv, roundOneDecimal(data.main.tempMax).toString())
        binding?.incWeatherContent?.lowTempTv?.text =
            getString(R.string.low_high_temp_tv, roundOneDecimal(data.main.tempMin).toString())
        binding?.incWeatherContent?.tempDescriptionTv?.text = data.weather[0].description
        binding?.incWeatherContent?.sunsetTv?.text = data.sys.sunset.unixTimestampToTimeString()
        binding?.incWeatherContent?.sunriseTv?.text = data.sys.sunrise.unixTimestampToTimeString()
        binding?.incWeatherContent?.humidityTv?.text =
            getString(R.string.humidity_tv, data.main.humidity.toString())
        binding?.incWeatherContent?.windchillTv?.text =
            getString(R.string.windchill_tv, roundOneDecimal(data.main.feelsLike).toString())
        binding?.incWeatherContent?.cloudinessTv?.text =
            getString(R.string.cloudiness_tv, data.clouds.all.toString())
        binding?.incWeatherContent?.pressureTv?.text =
            getString(R.string.pressure_tv, data.main.pressure.toString())
        binding?.incWeatherContent?.windSpeedTv?.text =
            getString(R.string.wind_speed_tv, data.wind.speed.toString())
        binding?.incWeatherContent?.visibilityTv?.text =
            getString(R.string.visibility_tv, data.visibility.toString())
    }

    private fun setupLocation() {
        locationProvider = LocationProvider(activity as AppCompatActivity)

        GpsUtils(activity as AppCompatActivity).turnGPSOn(
            object : GpsUtils.OnGpsListener {

                override fun gpsStatus(isGPSEnable: Boolean) {
                    isGPSEnabled = isGPSEnable
                }
            })

        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> showToast(activity as AppCompatActivity, getString(R.string.enable_gps), Toast.LENGTH_LONG)

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> requestLocationPermission()

            else -> requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.getCurrentLocation(locationProvider)
                } else {
                    showSnackbar(
                        binding?.root as View ,getString(R.string.accept_permission)
                    )
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    private fun startLocationUpdate() {
        viewModel.getCurrentLocation(locationProvider)
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    activity as AppCompatActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )


}