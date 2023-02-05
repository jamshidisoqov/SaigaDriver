package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenTripBinding
import uz.gita.saiga_driver.presentation.presenter.TripViewModelImpl
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.combine
import uz.gita.saiga_driver.utils.extensions.getFinanceType
import uz.gita.saiga_driver.utils.extensions.getFormat
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 2/5/2023
@AndroidEntryPoint
class TripScreen : Fragment(R.layout.screen_trip) {

    private val viewBinding: ScreenTripBinding by viewBinding()

    private val viewModel: TripViewModel by viewModels<TripViewModelImpl>()

    private val args: TripScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        viewModel.currentMoney.onEach {
            tvSum.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentWay.onEach {
            tvWay.text = it.getFormat(2).combine("km")
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentSpeed.onEach {
            tvSpeed.text = it.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        val intent = Intent(requireContext(), GpsService::class.java)
        requireContext().startService(intent)

        currentLocation.observe(viewLifecycleOwner) {
            viewModel.setCurrentLocation(it)
        }
    }
}