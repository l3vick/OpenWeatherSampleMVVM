package es.pablorojas.openweathersamplemvvm.ui.forecast

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.adapter.ForecastAdapter
import es.pablorojas.openweathersamplemvvm.core.BaseFragment
import es.pablorojas.openweathersamplemvvm.data.models.ResponseWeatherForecast
import es.pablorojas.openweathersamplemvvm.databinding.FragmentForecastBinding
import es.pablorojas.openweathersamplemvvm.utils.EXCLUDE
import es.pablorojas.openweathersamplemvvm.utils.Status
import es.pablorojas.openweathersamplemvvm.utils.getDrawableResID
import es.pablorojas.openweathersamplemvvm.utils.showSnackbar
import java.lang.ref.WeakReference

@AndroidEntryPoint
class ForecastFragment : BaseFragment<FragmentForecastBinding>(
    FragmentForecastBinding::inflate,
    R.layout.fragment_forecast
) {
    private val viewModel: ForecastViewModel by viewModels()

    private val args: ForecastFragmentArgs by navArgs()

    private var forecastAdapter = ForecastAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWeatherForecast(args.lat.toString(), args.lon.toString(), EXCLUDE)
    }

    override fun setupView() {
        binding?.incWeatherForecastContent?.cityTv?.text =
            getString(R.string.forecast_city_title, args.city)
        binding?.incWeatherForecastContent?.rvForecast?.apply {
            adapter = forecastAdapter
            setHasFixedSize(true)
        }
    }

    override fun setupListeners() {
    }

    override fun setupObservers() {
        viewModel.weatherForecast.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.incWeatherForecastContent?.root?.visibility = View.VISIBLE
                        it.data?.let { data -> setupUI(data) }
                    }
                    Status.ERROR -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.incWeatherForecastContent?.root?.visibility = View.GONE
                        showSnackbar(
                            binding?.incWeatherForecastContent?.root as View, it.message.toString()
                        )
                    }
                    Status.LOADING -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                        binding?.incWeatherForecastContent?.root?.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun setupUI(data: ResponseWeatherForecast) {
        binding?.incWeatherForecastContent?.weatherIv?.setImageResource(getDrawableResID(data.daily[0].weather[0].icon))
        binding?.incWeatherForecastContent?.tempTv?.text =
            getString(R.string.temp_tv, data.daily[0].temp.day.toString())
        binding?.incWeatherForecastContent?.lowTempTv?.text =
            getString(R.string.low_high_temp_tv, data.daily[0].temp.min.toString())
        binding?.incWeatherForecastContent?.highTempTv?.text =
            getString(R.string.low_high_temp_tv, data.daily[0].temp.max.toString())
        forecastAdapter.forecastList = data.daily.toMutableList().drop(1)
    }


}
