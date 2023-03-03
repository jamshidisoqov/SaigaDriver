package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.DialogChooseEndOrderBinding
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 2/25/2023
class ChooseEndOrderDialog : BottomSheetDialogFragment(R.layout.dialog_choose_end_order) {

    private val viewBinding: DialogChooseEndOrderBinding by viewBinding()

    private var endOrderCallback: (() -> Unit)? = null

    fun setEndOrderDialog(block: () -> Unit) {
        endOrderCallback = block
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        btnEndOrder.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                endOrderCallback?.invoke()
            }.launchIn(lifecycleScope)
    }


}