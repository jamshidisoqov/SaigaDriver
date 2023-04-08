package uz.gita.saiga_driver.presentation.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.databinding.DialogNearAddressBinding
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.combine
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 2/25/2023
class NearAddressDialog(private val orderResponse: OrderResponse) :
    DialogFragment(R.layout.dialog_near_address) {

    private val viewBinding: DialogNearAddressBinding by viewBinding()

    private var acceptListener: ((OrderResponse) -> Unit)? = null

    fun setAcceptListener(block: (OrderResponse) -> Unit) {
        acceptListener = block
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.apply {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        orderResponse.direction.apply {
            tvFromAddress.text =
                this.addressFrom.title ?: resources.getString(R.string.not_specified)
            tvToAddress.text = this.addressTo?.title ?: resources.getString(R.string.not_specified)
            tvDistance.text = resources.getString(R.string.order_near).combine(
                try {
                    calculationByDistance(
                        with(this.addressFrom) { LatLng(lat!!, lon!!) },
                        currentLocation.value ?: NUKUS
                    ).toString()
                } catch (_: Exception) {
                    "0.0 km"
                }
            )
        }

        btnDecline.setOnClickListener {
            dismiss()
        }

        btnAccept.setOnClickListener {
            acceptListener?.invoke(orderResponse)
            dismiss()
        }

        lifecycleScope.launchWhenResumed {
            var s = 5000
            while (s > 0) {
                delay(50)
                s -= 100
                progressHorizontalIndicator.progress = s / 50
            }
            dismiss()
        }

    }

}