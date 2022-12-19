package uz.gita.saiga_driver.presentation.ui.permission.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.presentation.presenter.PermissionsViewModelImpl
import uz.gita.saiga_driver.presentation.ui.permission.PermissionsViewModel
import uz.gita.saiga_driver.utils.NOTIFICATION_PERMISSION
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.toast

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class NotificationPage : Fragment(R.layout.page_notification) {

    private val viewModel: PermissionsViewModel by viewModels<PermissionsViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hasPermission(NOTIFICATION_PERMISSION, onPermissionGranted = {
            viewModel.navigateToMain()
        }) {
            toast("You couldn't use app if permissions granted")
        }
    }
}