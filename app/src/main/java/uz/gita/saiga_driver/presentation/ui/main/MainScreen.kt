package uz.gita.saiga_driver.presentation.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenMainBinding
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.GpsService
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {

    private val viewBinding: ScreenMainBinding by viewBinding()

    private var lastPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
        viewBinding.include {
            pagerMain.adapter = MainAdapter(requireActivity()) {
                viewBinding.bnvMain.itemActiveIndex = it
                lastPage = it
                pagerMain.setCurrentItem(it, true)
            }
            pagerMain.isUserInputEnabled = false
            bnvMain.setOnItemSelectedListener {
                pagerMain.setCurrentItem(it, true)
            }
            startGps()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        lastPage = viewBinding.pagerMain.currentItem
    }

    private fun startGps() {
        hasPermission(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), onPermissionGranted = {
                val intent = Intent(requireContext(), GpsService::class.java)
                requireContext().startService(intent)
            }) {}

    }

    override fun onResume() {
        super.onResume()
        viewBinding.bnvMain.itemActiveIndex = lastPage
    }
}