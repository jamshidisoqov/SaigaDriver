package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.DialogEndOrderBinding
import uz.gita.saiga_driver.utils.decFormat
import uz.gita.saiga_driver.utils.extensions.combine
import uz.gita.saiga_driver.utils.extensions.getCurrentTime
import uz.gita.saiga_driver.utils.extensions.getFinanceType
import uz.gita.saiga_driver.utils.extensions.getFormat
import uz.gita.saiga_driver.utils.extensions.include
import java.util.Date

// Created by Jamshid Isoqov on 2/18/2023
class EndOrderDialog(
    private val distance: Double,
    private val price: Double,
    private val startTime: Long,
    private val endTime: Long
) : DialogFragment(R.layout.dialog_end_order) {

    private val viewBinding: DialogEndOrderBinding by viewBinding()


    private var cancelListener: (() -> Unit)? = null

    fun setCancelListener(block: () -> Unit) {
        cancelListener = block
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

        try {

            tvDistance.text = distance.getFormat(2).combine("km")
            tvMoney.text = price.getFormat(2).toDouble().getFinanceType()

            tvStartTime.text = getCurrentTime(Date(startTime))

            tvEndTime.text = getCurrentTime(Date(endTime))

            btnClose.setOnClickListener {
                cancelListener?.invoke()
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}