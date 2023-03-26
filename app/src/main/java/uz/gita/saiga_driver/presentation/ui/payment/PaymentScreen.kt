package uz.gita.saiga_driver.presentation.ui.payment

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenPaymentBinding
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.callPhoneNumber
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.include

@AndroidEntryPoint
class PaymentScreen : Fragment(R.layout.screen_payment) {

    private val viewBinding: ScreenPaymentBinding by viewBinding()

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        btnPhone.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                phoneAdministrator()
            }.launchIn(lifecycleScope)
    }

    private fun phoneAdministrator() {
        hasPermission(permission = Manifest.permission.CALL_PHONE, onPermissionGranted = {
            callPhoneNumber("+998 99 454 11 96")
        }) {
            Snackbar.make(
                viewBinding.btnPhone,
                resources.getString(R.string.granted_phone),
                Snackbar.LENGTH_LONG
            ).setAction(resources.getString(R.string.check)) {
                phoneAdministrator()
            }.show()
        }
    }

}