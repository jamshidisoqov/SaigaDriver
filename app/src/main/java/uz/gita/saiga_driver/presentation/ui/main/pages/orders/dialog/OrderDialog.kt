package uz.gita.saiga_driver.presentation.ui.main.pages.orders.dialog

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.OrderData
import uz.gita.saiga_driver.databinding.DialogOrderAcceptBinding
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 12/19/2022
class OrderDialog(private val orderData: OrderData) :
    BottomSheetDialogFragment(R.layout.dialog_order_accept) {

    private val binding: DialogOrderAcceptBinding by viewBinding()

    private var acceptListener: ((OrderData) -> Unit)? = null

    fun setAcceptListener(block:(OrderData)->Unit){
        acceptListener = block
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.include {
        tvFromOrder.text = orderData.direction.addressFrom.title
        tvToOrder.text = orderData.direction.addressTo.title
        btnAccept.setOnClickListener {
            acceptListener?.invoke(orderData)
        }
    }
}