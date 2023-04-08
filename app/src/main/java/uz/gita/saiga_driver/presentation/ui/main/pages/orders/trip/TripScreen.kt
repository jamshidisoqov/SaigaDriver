package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse
import uz.gita.saiga_driver.databinding.ScreenTripBinding
import uz.gita.saiga_driver.presentation.presenter.TripViewModelImpl
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.dialog.ChooseEndOrderDialog
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.dialog.EndOrderDialog
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.*
import java.util.*

// Created by Jamshid Isoqov on 2/5/2023
@AndroidEntryPoint
class TripScreen : Fragment(R.layout.screen_trip) {

    private val viewBinding: ScreenTripBinding by viewBinding()

    private val viewModel: TripViewModel by viewModels<TripViewModelImpl>()

    private val args: TripScreenArgs by navArgs()

    private var isOrderActive = true

    private val startTime: Long by lazy(LazyThreadSafetyMode.NONE) { System.currentTimeMillis() }

    private val endTime: Long by lazy(LazyThreadSafetyMode.NONE) { System.currentTimeMillis() }

    private var distance = 0.0

    private var price = 8000.0

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

        viewModel.backSharedFlow.onEach {
            findNavController().navigateUp()
        }.launchIn(lifecycleScope)

        viewModel.openGoogleMapSharedFlow.onEach {
            openGoogleMap(args.order.direction.addressFrom, it)
        }.launchIn(lifecycleScope)

        tvFromUserName.text = with(args.order.fromUser) { this.firstName.combine(this.lastName) }

        tvFirstAddress.text = args.order.direction.addressFrom.title

        tvSecondAddress.text =
            args.order.direction.addressTo?.title ?: resources.getString(R.string.not_specified)

        cardCall.setOnClickListener {
            callPhone(args.order.fromUser.phoneNumber)
        }

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
                    viewModel.endOrder(orderResponse = args.order, startTime, endTime)
                    timerChr.stop()
                }
                isOrderActive = !isOrderActive
            }.launchIn(lifecycleScope)

        cardBackOrder.setOnClickListener {
            if (isOrderActive)
                viewModel.cancelOrder(args.order)
            else Snackbar.make(
                cardBackOrder,
                resources.getString(R.string.not_order_back),
                Snackbar.LENGTH_LONG
            )
                .setAction(resources.getString(R.string.phone)) {
                    callPhoneNumber(args.order.fromUser.phoneNumber)
                }
                .show()
        }

        val backPresentCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showChooseEndOrder()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPresentCallback
        )

        fabMapOrder.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.openGoogleMap()
            }.launchIn(lifecycleScope)

        cardPauseOrder.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                pauseOrder()
            }.launchIn(lifecycleScope)

        btnResumeOrder.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                resumeOrder()
            }.launchIn(lifecycleScope)
    }

    private fun showEndOrderDialog() {
        val dialog = EndOrderDialog(distance, price, startTime, endTime)
        dialog.setCancelListener {
            findNavController().navigateUp()
        }
        dialog.show(childFragmentManager, "end-order")
    }

    private fun pauseOrder() = viewBinding.include{
        viewModel.pauseOrder()
        timerPauseChr.base = SystemClock.elapsedRealtime()
        cardPauseOrder.gone()
        showPauseDialog()
    }

    private fun showPauseDialog() = viewBinding.include {
        pauseOrder.visible()
        timerPauseChr.start()
    }

    private fun resumeOrder() = viewBinding.include{
        viewModel.resumeOrder()
        cardPauseOrder.visible()
        pauseOrder.gone()
        timerPauseChr.stop()
    }

    private fun startTrip() = viewBinding.include {
        tvStartTime.text =
            resources.getString(R.string.start_time).combine(getCurrentTime(Date(startTime)))
        timerChr.base = SystemClock.elapsedRealtime()
        cardPauseOrder.visible()
        timerChr.start()
    }

    private fun showChooseEndOrder() {
        val dialog = ChooseEndOrderDialog()
        dialog.setEndOrderDialog {
            showEndOrderDialog()
        }
        dialog.show(childFragmentManager, "ChooseEndOrderDialog")
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

    private fun openGoogleMap(addressFrom: AddressResponse, end: LatLng) {
        val uri =
            "http://maps.google.com/maps?saddr=${addressFrom.lat},${addressFrom.lon}&daddr=${end.latitude},${end.longitude}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }
}