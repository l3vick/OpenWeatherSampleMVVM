package es.pablorojas.openweathersamplemvvm.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.pablorojas.openweathersamplemvvm.data.models.LocationData
import es.pablorojas.openweathersamplemvvm.data.repository.local.LocationProvider
import es.pablorojas.openweathersamplemvvm.utils.RequestCompleteListener
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : ViewModel()
