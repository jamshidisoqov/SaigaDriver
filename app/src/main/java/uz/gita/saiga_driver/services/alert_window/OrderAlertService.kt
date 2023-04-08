package uz.gita.saiga_driver.services.alert_window

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.databinding.LayoutOrderAlertViewBinding
import uz.gita.saiga_driver.utils.extensions.include
import javax.inject.Inject

@AndroidEntryPoint
class OrderAlertService : Service() {

    override fun onBind(p0: Intent?): IBinder? = null

    private var _binding: LayoutOrderAlertViewBinding? = null
    val binding: LayoutOrderAlertViewBinding get() = _binding!!

    @Inject
    lateinit var gson: Gson

    override fun onCreate() {
        super.onCreate()
        _binding = LayoutOrderAlertViewBinding.inflate(LayoutInflater.from(this))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        binding.include {
            intent?.let {
                val data = it.getStringExtra(INTENT_ORDER)
                data?.let {orderString->

                }
            }
        }
        return START_NOT_STICKY
    }

    companion object{
        const val INTENT_ORDER = "intent_order"
    }


}