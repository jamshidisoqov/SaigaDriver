package uz.gita.saiga_driver.utils.extensions

import android.app.Activity
import android.graphics.Color
import androidx.annotation.StringRes
import com.tapadoo.alerter.Alerter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.domain.repository.OrderRepository

fun Activity.showAlerter(
    orderId: Long,
    title: String,
    text: String,
    @StringRes buttonId: Int,
    coroutineScope: CoroutineScope,
    orderRepository: OrderRepository,
    onSuccess: () -> Unit,
    onMessage: (String) -> Unit,
    onError: (Throwable) -> Unit
) {
    Alerter.create(this)
        .setTitle(title)
        .setText("Distance".combine(text))
        .setDuration(5000)
        .setIcon(R.drawable.saiga_yellow_car)
        .setIconSize(R.dimen.icon_size_alerter)
        .setIconColorFilter(Color.TRANSPARENT)
        .addButton(resources.getString(buttonId)) {
            coroutineScope.launch(Dispatchers.IO) {
                orderRepository.receiveOrder(orderId = orderId)
                    .collectLatest { result ->
                        result.onSuccess {
                            onSuccess.invoke()
                        }.onMessage { message ->
                            onMessage.invoke(message)
                        }.onError {
                            onError.invoke(it)
                        }
                    }
            }
        }
        .setBackgroundColorInt(Color.parseColor("#975C28"))
        .show()


}