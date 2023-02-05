package es.pablorojas.openweathersamplemvvm.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
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

    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLocation()
    }

    override fun setupView() {

    }

    override fun setupListeners() {
        binding?.incWeatherContent?.forecastBtn?.setOnClickListener {
            navController.navigate(R.id.action_weatherFragment_to_forecastFragment)
        }
    }

    override fun setupObservers() {
        viewModel.locationLiveData.observe(this) {
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

    private fun getDrawableResID(icon: String): Int {
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

    private fun setupLocation() {//checking GPS status

        locationProvider = LocationProvider(activity as AppCompatActivity)

        GpsUtils(activity as AppCompatActivity).turnGPSOn(
            object : GpsUtils.OnGpsListener {

                override fun gpsStatus(isGPSEnable: Boolean) {
                    isGPSEnabled = isGPSEnable
                }
            })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> showToast(activity as AppCompatActivity, "Enable GPS", 1)

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
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
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