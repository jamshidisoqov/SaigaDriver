package uz.gita.saiga_driver.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenMainBinding
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {

    private val viewBinding: ScreenMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
        viewBinding.include {

            pagerMain.adapter = MainAdapter(requireActivity())

            pagerMain.isUserInputEnabled = false

            bnvMain.setOnItemSelectedListener {
                val pos = when (it.itemId) {
                    R.id.menu_home -> 0
                    R.id.menu_orders -> 1
                    else -> 2
                }
                pagerMain.setCurrentItem(pos, true)
                true
            }
        }
}