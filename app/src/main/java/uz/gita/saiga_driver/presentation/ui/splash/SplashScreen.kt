package uz.gita.saiga_driver.presentation.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.presentation.presenter.SplashViewModelImpl
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.GpsService
import uz.gita.saiga_driver.utils.extensions.checkLocation
import uz.gita.saiga_driver.utils.extensions.hasPermission
import javax.inject.Inject


// Created by Jamshid Isoqov on 12/12/2022
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    @Inject
    lateinit var mySharedPref: MySharedPref

    private var dialogIsShowing = false
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(1000)
            if (mySharedPref.introFinished) {
                if (mySharedPref.isVerify) {
                    statusCheck()
                } else {
                    viewModel.navigateToScreen()
                }
            } else {
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToIntroScreen())
            }
        }
    }

    private fun statusCheck() {
        requireContext().checkLocation(dialogIsShowing) {
            if (it) {
                hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, onPermissionGranted = {
                    val intent = Intent(requireContext(), GpsService::class.java)
                    requireContext().startService(intent)
                }, onPermissionDenied = {})
                findNavController().navigate(R.id.action_splashScreen_to_mainScreen)
            } else {
                statusCheck()
            }
        }
    }
}