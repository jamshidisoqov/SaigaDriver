package uz.gita.saiga_driver.presentation.ui.permission

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenPermissionsCheckBinding
import uz.gita.saiga_driver.presentation.presenter.PermissionsViewModelImpl
import uz.gita.saiga_driver.presentation.ui.permission.adapter.PermissionsAdapter
import uz.gita.saiga_driver.utils.LOCATION_COARSE_PERMISSION
import uz.gita.saiga_driver.utils.LOCATION_PERMISSION
import uz.gita.saiga_driver.utils.NOTIFICATION_PERMISSION
import uz.gita.saiga_driver.utils.PageEventListener
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.onPageCallback
import uz.gita.saiga_driver.utils.extensions.toast

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class PermissionsCheckScreen : Fragment(R.layout.screen_permissions_check) {

    private val binding: ScreenPermissionsCheckBinding by viewBinding()

    private val viewModel:PermissionsViewModel by viewModels<PermissionsViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.include {
        pagerPermission.adapter = PermissionsAdapter(requireActivity())
        pagerPermission.isUserInputEnabled = false
        pagerPermission.onPageCallback { position ->
            if (position == 1) {
                fabNextPage.setImageResource(R.drawable.ic_selected)
            } else {
                fabNextPage.setImageResource(R.drawable.ic_next)
            }
        }
        fabNextPage.setOnClickListener {
            if (pagerPermission.currentItem == 1) {
                viewModel.navigateToMain()
            } else {
                hasPermission(
                    permissions = listOf(
                        LOCATION_PERMISSION,
                        LOCATION_COARSE_PERMISSION,
                        NOTIFICATION_PERMISSION
                    ), onPermissionGranted = {
                        viewModel.navigateToMain()
                    }) {
                    toast("You couldn't use app if permissions granted")
                }
            }
        }
        PageEventListener.pageListener.observe(viewLifecycleOwner) {
            pagerPermission.setCurrentItem(it, true)
        }
    }

}