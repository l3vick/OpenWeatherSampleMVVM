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
import es.pablorojas.openweathersamplemvvm.data.repository.local.LocationProvider
import es.pablorojas.openweathersamplemvvm.databinding.FragmentWeatherBinding
import es.pablorojas.openweathersamplemvvm.utils.GpsUtils
import es.pablorojas.openweathersamplemvvm.utils.Status
import es.pablorojas.openweathersamplemvvm.utils.showToast

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding>(FragmentWeatherBinding::inflate, R.layout.fragment_weather) {

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
        binding?.content?.forecastBtn?.setOnClickListener {
            navController.navigate(R.id.action_weatherFragment_to_forecastFragment)
        }
    }

    override fun setupObservers() {
        viewModel.locationLiveData.observe(this) {
            viewModel.getWeatherByLocation(it.latitude.toString(),it.longitude.toString())
        }

        viewModel.weatherByLocation.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        context?.let { it1 -> showToast(it1, it.data?.name.toString(), Toast.LENGTH_LONG) }
                        /*inc_info_weather.visibility= View.VISIBLE
                        progressBar.visibility= View.GONE
                        anim_failed.visibility= View.GONE
                        anim_network.visibility= View.GONE
                        setUpUI(it.data)
                        viewModel.updateSavedCities(cityRepo, CityUpdate(it.data?.id,1))*/
                    }
                    Status.ERROR -> {
                        // showFailedView(it.message)
                    }
                    Status.LOADING -> {
                        /*progressBar.visibility= View.VISIBLE
                        anim_failed.visibility= View.GONE
                        anim_network.visibility= View.GONE*/
                    }
                }
            }
        }

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
            !isGPSEnabled -> showToast(activity as AppCompatActivity,"Enable GPS",1)

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