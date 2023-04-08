package uz.gita.saiga_driver.presentation.ui.direction.add.address

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges
import uz.gita.saiga_driver.MainActivity
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse
import uz.gita.saiga_driver.databinding.DialogOrderMapBinding
import uz.gita.saiga_driver.presentation.presenter.AddressViewModelImpl
import uz.gita.saiga_driver.utils.DEBOUNCE_TEXT_CHANGES
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 2/24/2023
@AndroidEntryPoint
class AddressDialog : BottomSheetDialogFragment(R.layout.dialog_order_map) {

    private val viewBinding: DialogOrderMapBinding by viewBinding()

    private val viewModel: AddressViewModel by viewModels<AddressViewModelImpl>()

    private var addressCallback: ((AddressResponse) -> Unit)? = null

    private var mapCallback: ((Unit) -> Unit)? = null

    fun setAddressCallback(block: (AddressResponse) -> Unit) {
        addressCallback = block
    }

    fun setMapCallback(block: (Unit) -> Unit) {
        mapCallback = block
    }

    private var addressClicked = false

    private val adapter: AddressAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddressAdapter()
    }

    private lateinit var behavior: BottomSheetBehavior<*>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        behavior = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        MainActivity.activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        listDirections.adapter = adapter


        viewModel.allAddressFlow.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        adapter.setItemClickListener {
            addressCallback?.invoke(it)
            inputFrom.setText(it.title)
            addressClicked = true
            btnSave.isEnabled = true
        }

        btnSave.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                dismiss()
            }.launchIn(lifecycleScope)

        inputFrom.textChanges()
            .debounce(DEBOUNCE_TEXT_CHANGES)
            .onEach {
                if (!addressClicked) {
                    btnSave.isEnabled = false
                }
                addressClicked = false
                viewModel.searchAddress(it.toString())
            }.launchIn(lifecycleScope)

        imageMap.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                mapCallback?.invoke(Unit)
                dismiss()
            }.launchIn(lifecycleScope)


    }
}