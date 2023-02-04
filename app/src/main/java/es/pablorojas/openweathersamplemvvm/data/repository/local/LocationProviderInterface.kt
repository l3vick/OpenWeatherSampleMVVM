package es.pablorojas.openweathersamplemvvm.data.repository.local


import es.pablorojas.openweathersamplemvvm.data.models.LocationData
import es.pablorojas.openweathersamplemvvm.utils.RequestCompleteListener

interface LocationProviderInterface {
    fun getUserCurrentLocation(callback: RequestCompleteListener<LocationData>)
}