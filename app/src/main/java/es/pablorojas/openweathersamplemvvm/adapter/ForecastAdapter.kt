package es.pablorojas.openweathersamplemvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.data.models.Daily
import es.pablorojas.openweathersamplemvvm.databinding.ItemForecastBinding
import es.pablorojas.openweathersamplemvvm.utils.getDrawableResID
import es.pablorojas.openweathersamplemvvm.utils.getNameDayFromTimestamp
import es.pablorojas.openweathersamplemvvm.utils.roundOneDecimal
import java.lang.ref.WeakReference


class ForecastAdapter() : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    lateinit var forecastList: List<Daily>
    lateinit var context: WeakReference<Context>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding =
            ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = WeakReference(parent.context)
        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }


    inner class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(daily: Daily) {
            binding.dayNameTv.text = getNameDayFromTimestamp(daily.dt.toLong())
            binding.highTempTv.text = context.get()
                ?.getString(R.string.low_high_temp_tv, roundOneDecimal(daily.temp.max).toString())
            binding.lowTempTv.text = context.get()
                ?.getString(R.string.low_high_temp_tv, roundOneDecimal(daily.temp.min).toString())
            binding.weatherIv?.setImageResource(getDrawableResID(daily.weather[0].icon))
        }
    }
}