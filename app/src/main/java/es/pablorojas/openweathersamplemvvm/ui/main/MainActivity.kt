package es.pablorojas.openweathersamplemvvm.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.core.BaseActivity
import es.pablorojas.openweathersamplemvvm.data.repository.local.LocationProvider
import es.pablorojas.openweathersamplemvvm.utils.GpsUtils
import es.pablorojas.openweathersamplemvvm.utils.LOCATION_REQUEST
import es.pablorojas.openweathersamplemvvm.utils.showToast

@AndroidEntryPoint
class MainActivity(override val layoutResId: Int = R.layout.activity_main) : BaseActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

}