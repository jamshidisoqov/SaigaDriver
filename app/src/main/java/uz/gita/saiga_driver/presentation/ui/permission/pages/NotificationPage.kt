package uz.gita.saiga_driver.presentation.ui.permission.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.PageNotificationBinding
import uz.gita.saiga_driver.presentation.presenter.PermissionsViewModelImpl
import uz.gita.saiga_driver.presentation.ui.permission.PermissionsViewModel
import uz.gita.saiga_driver.utils.NOTIFICATION_PERMISSION
import uz.gita.saiga_driver.utils.extensions.hasPermission

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class NotificationPage : Fragment(R.layout.page_notification) {

    private val viewModel: PermissionsViewModel by viewModels<PermissionsViewModelImpl>()

    private val viewBinding: PageNotificationBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkPermission()
    }

    private fun checkPermission() {
        hasPermission(
            permission = NOTIFICATION_PERMISSION,
            onPermissionGranted = { viewModel.navigateToMain() }) {
            Snackbar.make(
                viewBinding.imageNotification, "Permission denied notification",
                Snackbar.LENGTH_SHORT
            ).setAction("Try check") { checkPermission() }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        viewModel.navigateToMain()
                    }
                })
                .show()
        }
    }
}