package es.pablorojas.openweathersamplemvvm.ui.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.core.BaseFragment
import es.pablorojas.openweathersamplemvvm.databinding.FragmentWeatherBinding

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding>(FragmentWeatherBinding::inflate, R.layout.fragment_weather) {

    override fun setupView() {

    }

    override fun setupListeners() {
        binding?.forecastBtn?.setOnClickListener {
            navController.navigate(R.id.action_weatherFragment_to_forecastFragment)
        }
    }

    override fun setupObservers() {
    }


}