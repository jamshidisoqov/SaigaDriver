package uz.gita.saiga_driver.presentation.ui.permission.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.presentation.ui.permission.PermissionsCheckScreenDirections
import uz.gita.saiga_driver.utils.NOTIFICATION_PERMISSION
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.toast

// Created by Jamshid Isoqov on 12/12/2022
class NotificationPage : Fragment(R.layout.page_notification) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        hasPermission(NOTIFICATION_PERMISSION, onPermissionGranted = {
            findNavController().navigate(PermissionsCheckScreenDirections.actionPermissionsCheckScreenToMainScreen())
        }) {
            toast("You couldn't use app if permissions granted")
        }
    }
}