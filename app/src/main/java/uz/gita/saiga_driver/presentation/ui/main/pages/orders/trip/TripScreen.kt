package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenTripBinding
import uz.gita.saiga_driver.presentation.presenter.TripViewModelImpl
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.dialog.EndOrderDialog
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 2/5/2023
@AndroidEntryPoint
class TripScreen : Fragment(R.layout.screen_trip) {

    private val viewBinding: ScreenTripBinding by viewBinding()

    private val viewModel: TripViewModel by viewModels<TripViewModelImpl>()

    private val args: TripScreenArgs by navArgs()

    private var isOrderActive = true

    private var distance = 0.0

    private var price = 7000.0

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        viewModel.currentMoney.onEach {
            price = it
            tvSum.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentWay.onEach {
            distance = it
            tvWay.text = it.getFormat(2).combine("km")
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentSpeed.onEach {
            tvSpeed.text = it.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.endOrderDialog.onEach {
            showEndOrderDialog()
        }.launchIn(lifecycleScope)

        tvFromUserName.text = with(args.order.fromUser) { this.firstName.combine(this.lastName) }

        cardCall.setOnClickListener {
                log("cardCall")
                callPhone(args.order.fromUser.phoneNumber)
            }

        val intent = Intent(requireContext(), GpsService::class.java)
        requireContext().startService(intent)
        currentLocation.observe(viewLifecycleOwner) {
            viewModel.setCurrentLocation(it, !isOrderActive)
        }

        cardOrderStatus
            .clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                if (isOrderActive) {
                    startTrip()
                    fabMapOrder.gone()
                    cardOrderStatus.setCardBackgroundColor(Color.parseColor("#D61E1E"))
                    tvOrderStatus.text = getStringResource(resId = R.string.end_order)
                } else {
                    viewModel.endOrder(orderResponse = args.order)
                }
                isOrderActive = !isOrderActive
            }.launchIn(lifecycleScope)

        cardBackOrder.setOnClickListener {
                log("cardBackOrder")
                viewModel.cancelOrder(args.order)
            }


        fabMapOrder.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToMap(args.order)
            }.launchIn(lifecycleScope)
    }

    private fun showEndOrderDialog() {
        val dialog = EndOrderDialog(distance, price)
        dialog.setCancelListener {
            findNavController().navigateUp()
        }
        dialog.show(childFragmentManager, "end-order")
    }

    private fun startTrip() {

    }

    private fun callPhone(phone: String) {
        hasPermission(
            permission = Manifest.permission.CALL_PHONE,
            onPermissionGranted = {
                callPhoneNumber(phone)
            },
            onPermissionDenied = {
                Snackbar.make(
                    viewBinding.fabMapOrder, "Permission denied",
                    Snackbar.LENGTH_SHORT
                ).setAction("Try check") {
                    callPhone(phone)
                }
                    .show()
            })
    }
}