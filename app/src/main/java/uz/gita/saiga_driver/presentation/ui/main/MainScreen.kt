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

    private var lastPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
        viewBinding.include {
            pagerMain.adapter = MainAdapter(requireActivity()) {
                viewBinding.bnvMain.itemActiveIndex = it
                pagerMain.setCurrentItem(it, true)
            }
            pagerMain.isUserInputEnabled = false
            bnvMain.setOnItemSelectedListener {
                pagerMain.setCurrentItem(it, true)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        lastPage = viewBinding.pagerMain.currentItem
    }

    override fun onResume() {
        super.onResume()
        viewBinding.bnvMain.itemActiveIndex = lastPage
    }
}