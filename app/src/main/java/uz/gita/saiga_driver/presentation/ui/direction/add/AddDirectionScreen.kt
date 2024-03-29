package uz.gita.saiga_driver.presentation.ui.direction.add

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.itemClickEvents
import ru.ldralighieri.corbind.widget.textChanges
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.StaticAddressResponse
import uz.gita.saiga_driver.databinding.ScreenAddDirectionBinding
import uz.gita.saiga_driver.presentation.dialogs.ChooseDateDialog
import uz.gita.saiga_driver.presentation.dialogs.ChooseTimeDialog
import uz.gita.saiga_driver.presentation.dialogs.map.MapDialog
import uz.gita.saiga_driver.presentation.presenter.AddDirectionViewModelImpl
import uz.gita.saiga_driver.presentation.ui.direction.add.address.AddressDialog
import uz.gita.saiga_driver.utils.DEBOUNCE_TEXT_CHANGES
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.MaskWatcherPayment
import uz.gita.saiga_driver.utils.extensions.*
import java.util.*
import kotlin.math.min

// Created by Jamshid Isoqov on 12/13/2022
@AndroidEntryPoint
class AddDirectionScreen : Fragment(R.layout.screen_add_direction) {

    private val viewModel: AddDirectionViewModel by viewModels<AddDirectionViewModelImpl>()

    private val viewBinding: ScreenAddDirectionBinding by viewBinding()
    private var fromAddress: Pair<String, LatLng>? = null
    private var toAddress: Pair<String, LatLng>? = null
    private var date: Date? = null
    private var time: String? = null
    private var boolFromAddress: Boolean = false
    private var boolToAddress: Boolean = false
    private var boolPrice: Boolean = false
    private var isFromFocus = true
    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        viewModel.loadingSharedFlow.onEach {
            if (it) showProgress() else hideProgress()
        }.launchIn(lifecycleScope)

        viewModel.errorSharedFlow.onEach {
            showErrorDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow.onEach {
            showMessageDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.backFlow.onEach {
            findNavController().navigateUp()
        }.launchIn(lifecycleScope)

        //events

        actWhereFrom.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                isFromFocus = true
                showAddressDialog()
            }.launchIn(lifecycleScope)

        actWhereTo.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                isFromFocus = false
                showAddressDialog()
            }.launchIn(lifecycleScope)

        inputAmount.apply {
            addTextChangedListener(MaskWatcherPayment(this))
            textChanges()
                .debounce(DEBOUNCE_TEXT_CHANGES)
                .onEach {
                    boolPrice = it.toString().isNotEmpty()
                    isOrderEnabled()
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
        imageMapFrom.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                val dialog = MapDialog("Where from", fromAddress?.second)
                dialog.setMapListener { title, address ->
                    fromAddress = Pair(title, address)
                    actWhereFrom.setText(title.substring(0, min(title.length,30)))
                    boolFromAddress = title.isNotEmpty()
                    isOrderEnabled()
                }
                dialog.show(childFragmentManager, "from address")
            }.launchIn(lifecycleScope)

        imageMapTo.clicks()
            .onEach {
                val dialog = MapDialog("Where to", toAddress?.second)
                dialog.setMapListener { title, address ->
                    toAddress = Pair(title, address)
                    actWhereTo.setText(title.substring(0,min(title.length,30)))
                    boolToAddress = title.isNotEmpty()
                    isOrderEnabled()
                }
                dialog.show(childFragmentManager, "to address")
            }.launchIn(lifecycleScope)

        inputDate.isFocusable = false
        inputTime.isFocusable = false

        inputDate
            .clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                openDate()
            }.launchIn(lifecycleScope)

        inputTime
            .clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                openTime()
            }.launchIn(lifecycleScope)

        btnAddDirection
            .clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                val schedule = if (date != null) getCurrentDate(date!!) else null
                viewModel.addDirection(
                    whereFrom = fromAddress!!,
                    whereTo = toAddress!!,
                    schedule = schedule.combine(time?: getCurrentTime(Date())),
                    price = inputAmount.text.toString().getDigitOnly(),
                    comment = inputComment.text.toString()
                )
            }.launchIn(lifecycleScope)

        viewModel.getAllStaticAddress()

    }
    private fun showAddressDialog() = viewBinding.include{
        val dialog = AddressDialog()
        dialog.setAddressCallback {
            if (isFromFocus) {
                boolFromAddress = true
                actWhereFrom.setText(it.title ?: "")
                fromAddress = Pair(it.title?: "",with(it) { LatLng(lat!!, lon!!) })
            } else {
                boolToAddress = true
                actWhereTo.setText(it.title ?: "")
                toAddress = Pair(it.title ?: "",with(it) { LatLng(lat!!, lon!!) })
            }
        }
        dialog.setMapCallback {
            //TODO
        }
        dialog.show(childFragmentManager, "AddressDialog")
    }


    private fun openTime() = viewBinding.include {
        val timeDialog = ChooseTimeDialog(requireContext(), time ?: "12:00")
        timeDialog.setTimeListener {
            time = it
            inputTime.setText(it)
        }
        timeDialog.show()
    }

    private fun openDate() = viewBinding.include {
        val dateDialog = ChooseDateDialog(requireContext(), date ?: Date())
        dateDialog.setConfirmClickListener {
            date = it
            inputDate.setText(getCurrentDate(it))
        }
        dateDialog.show()
    }

    private fun isOrderEnabled() {
        viewBinding.btnAddDirection.isEnabled = boolFromAddress && boolToAddress && boolPrice
    }
}