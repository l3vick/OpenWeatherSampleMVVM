package es.pablorojas.openweathersamplemvvm.ui.main

import dagger.hilt.android.AndroidEntryPoint
import es.pablorojas.openweathersamplemvvm.R
import es.pablorojas.openweathersamplemvvm.core.BaseActivity

@AndroidEntryPoint
class MainActivity(override val layoutResId: Int = R.layout.activity_main) : BaseActivity()