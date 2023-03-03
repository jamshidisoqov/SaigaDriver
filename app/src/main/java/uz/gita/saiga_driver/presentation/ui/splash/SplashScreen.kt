package uz.gita.saiga_driver.presentation.ui.splash

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.presentation.presenter.SplashViewModelImpl
import uz.gita.saiga_driver.utils.extensions.checkLocation
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
        if (mySharedPref.token.isEmpty()) {
            viewModel.navigateToScreen()
        } else if (mySharedPref.isVerify)
            statusCheck()
    }

    private fun statusCheck() {
        requireContext().checkLocation(dialogIsShowing) {
            if (it) {
                findNavController().navigate(R.id.action_splashScreen_to_mainScreen)
            } else {
                statusCheck()
            }
        }
    }
}