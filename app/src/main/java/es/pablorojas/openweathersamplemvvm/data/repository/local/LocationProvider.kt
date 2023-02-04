package es.pablorojas.openweathersamplemvvm.data.repository.local

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import es.pablorojas.openweathersamplemvvm.data.models.LocationData
import es.pablorojas.openweathersamplemvvm.utils.RequestCompleteListener

class LocationProvider(context: Context) : LocationProviderInterface {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getUserCurrentLocation(callback: RequestCompleteListener<LocationData>) {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            it.also { callback.onRequestCompleted(setLocationData(it)) }
        }.addOnFailureListener {
            callback.onRequestFailed(it.localizedMessage)
        }

        startLocationUpdates()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun setLocationData(location: Location): LocationData {
        return LocationData(longitude = location.longitude, latitude = location.latitude)
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}