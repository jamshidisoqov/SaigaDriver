package uz.gita.saiga_driver.presentation.ui.direction.add

import android.os.Bundle
import android.view.View
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
import ru.ldralighieri.corbind.widget.textChanges
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.databinding.ScreenAddDirectionBinding
import uz.gita.saiga_driver.presentation.dialogs.ChooseDateDialog
import uz.gita.saiga_driver.presentation.dialogs.ChooseTimeDialog
import uz.gita.saiga_driver.presentation.dialogs.map.MapDialog
import uz.gita.saiga_driver.presentation.presenter.AddDirectionViewModelImpl
import uz.gita.saiga_driver.utils.DEBOUNCE_TEXT_CHANGES
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.MaskWatcherPayment
import uz.gita.saiga_driver.utils.extensions.*
import java.util.*

// Created by Jamshid Isoqov on 12/13/2022
@AndroidEntryPoint
class AddDirectionScreen : Fragment(R.layout.screen_add_direction) {

    private val viewModel: AddDirectionViewModel by viewModels<AddDirectionViewModelImpl>()

    private val viewBinding: ScreenAddDirectionBinding by viewBinding()
    private var fromAddress: Pair<String, LatLng>? = null
    private var toAddress: Pair<String, LatLng>? = null
    private var date: Date? = null
    private var time: String? = null
    private var allDirection = emptyList<DirectionResponse>()
    private var boolFromAddress: Boolean = false
    private var boolToAddress: Boolean = false
    private var boolPrice: Boolean = false

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        viewModel.allDirections.onEach {
            allDirection = it
        }.launchIn(lifecycleScope)

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
                    actWhereFrom.setText(title)
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
                    actWhereTo.setText(title)
                    boolToAddress = title.isNotEmpty()
                    isOrderEnabled()
                }
                dialog.show(childFragmentManager, "to address")
            }.launchIn(lifecycleScope)

        tilDate
            .clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                openDate()
            }.launchIn(lifecycleScope)

        tilTime
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
                    schedule = schedule,
                    price = inputAmount.text.toString().getDigitOnly().toDouble(),
                    comment = inputComment.text.toString()
                )
            }.launchIn(lifecycleScope)


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