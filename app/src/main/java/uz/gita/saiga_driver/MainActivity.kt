package uz.gita.saiga_driver

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.broadcast.LocationManageBroadcast
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.di.DatabaseModule
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.navigation.NavigationHandler
import uz.gita.saiga_driver.presentation.dialogs.CheckConnectionDialog
import uz.gita.saiga_driver.presentation.dialogs.ProgressDialog
import uz.gita.saiga_driver.services.notification.NotificationService
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.driverStatusLiveData
import uz.gita.saiga_driver.utils.extensions.*
import uz.gita.saiga_driver.utils.socketStatusLiveData
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mySharedPref: MySharedPref

    private lateinit var dialog: ProgressDialog

    @Inject
    lateinit var navigationHandler: NavigationHandler

    @Inject
    lateinit var repository: MapRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    private lateinit var controller: NavController

    @Inject
    lateinit var gson: Gson

    @SuppressLint("ServiceCast", "MissingPermission", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this

        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navigationHandler.navigationStack
            .onEach { it.invoke(fragment.findNavController()) }
            .launchIn(lifecycleScope)

        dialog = ProgressDialog(this)

        controller = fragment.navController

        DatabaseModule.unauthorizedLiveData.observe(this) {
            mySharedPref.isVerify = false
            fragment.navController.navigate(R.id.action_global_loginScreen)
        }

        setLocale()

        registerLocationManager()

        driverStatusLiveData.observe(this) {
            if (it) {
                orderRepository.socketConnect()
            } else {
                orderRepository.socketDisconnect()
            }
        }
        socketStatusLiveData.observe(this) {
            val dialog = CheckConnectionDialog()
            dialog.setOnCheckListener {
                orderRepository.socketConnect()
            }
            dialog.show(supportFragmentManager, "Check connection")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        orderRepository.socketDisconnect()
    }

    fun showProgress() {
        dialog.show()
    }

    fun hideProgress() {
        dialog.cancel()
    }

    companion object {
        lateinit var activity: MainActivity

        private val language = listOf("uz", "en", "kaa", "ru")

    }

    private fun registerLocationManager() {
        val locationManagerReceiver = LocationManageBroadcast()

        locationManagerReceiver.setReceiverCallBack { s, b ->
            when (s) {
                Intent.ACTION_PROVIDER_CHANGED -> {
                    if (!b) {
                        statusCheck()
                    } else {
                        controller.navigate(R.id.action_global_splashScreen)
                    }
                }
                Intent.ACTION_AIRPLANE_MODE_CHANGED -> {

                }
                WifiManager.NETWORK_STATE_CHANGED_ACTION -> {

                }
            }
        }
        registerReceiver(
            locationManagerReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_PROVIDER_CHANGED)
                addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
                addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            })
    }

    fun createNotification(orderResponse: OrderResponse, showNotification: Boolean = false) {
        try {
            if (!showNotification)
                showAlerter(orderResponse)
            val intent = Intent(this, NotificationService::class.java)
            intent.putExtra("order", gson.toJsonData(orderResponse))
            startService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showAlerter(orderResponse: OrderResponse) {
        try {
            lifecycleScope.launch {
                val fromAddress = with(orderResponse.direction.addressFrom) {
                    LatLng(lat!!, lon!!)
                }
                Alerter.create(this@MainActivity)
                    .setTitle(orderResponse.direction.addressFrom.title!!)
                    .setText(
                        "Distance".combine(
                            distance(
                                fromAddress,
                                currentLocation.value ?: NUKUS
                            ).toString()
                        )
                    )
                    .setDuration(3000)
                    .setIcon(R.drawable.saiga_yellow_car)
                    .setIconSize(R.dimen.icon_size_alerter)
                    .setIconColorFilter(Color.TRANSPARENT)
                    .addButton("Accept") {
                        lifecycleScope.launch {
                            orderRepository.receiveOrder(orderResponse.id)
                                .collectLatest { result ->
                                    result.onSuccess {
                                        controller.navigate(
                                            NavGraphDirections.actionGlobalTripScreen(
                                                orderResponse.copy(distance = "")
                                            )
                                        )
                                    }.onMessage { message ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.onError {
                                        Toast.makeText(
                                            this@MainActivity,
                                            it.getMessage(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                    .setBackgroundColorInt(Color.parseColor("#975C28"))
                    .show()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLocale() {
        val localeName = language[mySharedPref.language]
        val locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)
    }

    fun setNewLocale() {
        val refresh = Intent(this, MainActivity::class.java)
        refresh.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        this.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(refresh)
    }

    private fun statusCheck() {
        checkLocation(true) {
            if (!it) {
                statusCheck()
            }
        }
    }

}