package uz.gita.saiga_driver

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.navigation.NavigationHandler
import uz.gita.saiga_driver.presentation.dialogs.ProgressDialog
import uz.gita.saiga_driver.services.notification.NotificationService
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.combine
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.extensions.toJsonData
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    }

    fun showProgress() {
        dialog.show()
    }

    fun hideProgress() {
        dialog.cancel()
    }

    companion object {
        lateinit var activity: MainActivity
    }

    fun createNotification(orderResponse: OrderResponse, showNotification: Boolean = false) {
        if (!showNotification)
            showAlerter(orderResponse)
        val intent = Intent(this, NotificationService::class.java)
        intent.putExtra("order", gson.toJsonData(orderResponse))
        startService(intent)
    }

    private fun showAlerter(orderResponse: OrderResponse) {

        lifecycleScope.launch {


            val fromAddress = with(orderResponse.direction.addressFrom) {
                LatLng(lat!!, lon!!)
            }

            repository.getAddressByLocation(fromAddress).collectLatest { result ->
                result.onSuccess {
                    Alerter.create(this@MainActivity)
                        .setTitle(it)
                        .setText(
                            "Distance".combine(
                                calculationByDistance(
                                    fromAddress,
                                    currentLocation.value ?: NUKUS
                                ).toString()
                            )
                        )
                        .setDuration(5000)
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
            }
        }
    }
}