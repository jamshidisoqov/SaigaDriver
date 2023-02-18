package uz.gita.saiga_driver

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.saiga_driver.app.App
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.navigation.NavigationHandler
import uz.gita.saiga_driver.presentation.dialogs.ProgressDialog
import uz.gita.saiga_driver.services.notification.NotificationService
import uz.gita.saiga_driver.utils.extensions.toJsonData
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var dialog: ProgressDialog

    @Inject
    lateinit var navigationHandler: NavigationHandler

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

    fun createNotification(orderResponse: OrderResponse){
        val intent = Intent(this, NotificationService::class.java)
        intent.putExtra("order",gson.toJsonData(orderResponse))
        startService(intent)
    }
}