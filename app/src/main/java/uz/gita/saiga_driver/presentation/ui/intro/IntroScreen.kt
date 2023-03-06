package uz.gita.saiga_driver.presentation.ui.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.databinding.ScreenIntroBinding
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.onPageCallback
import javax.inject.Inject

// Created by Jamshid Isoqov on 3/6/2023

@AndroidEntryPoint
class IntroScreen : Fragment(R.layout.screen_intro) {

    @Inject
    lateinit var mySharedPref: MySharedPref

    private val viewBinding: ScreenIntroBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        viewPagerIntro.adapter = IntroAdapter(requireActivity())
        dotsIndicator.attachTo(viewPagerIntro)

        viewPagerIntro.onPageCallback {
            if (it == 2) {
                mySharedPref.introFinished = true
            }
        }

        btnGetStarted.setOnClickListener {
            mySharedPref.introFinished = true
            findNavController().navigate(IntroScreenDirections.actionIntroScreenToLoginScreen())
        }
    }
}