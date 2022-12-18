package uz.gita.saiga_driver.presentation.ui.permission.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.utils.LOCATION_COARSE_PERMISSION
import uz.gita.saiga_driver.utils.LOCATION_PERMISSION
import uz.gita.saiga_driver.utils.PageEventListener
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.toast

// Created by Jamshid Isoqov on 12/12/2022
class LocationPage : Fragment(R.layout.page_location) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hasPermission(
            permissions = listOf(
                LOCATION_PERMISSION,
                LOCATION_COARSE_PERMISSION
            ), onPermissionGranted = {
                PageEventListener.pageListener.value = 1
            }) {
            toast("You couldn't use app if permissions granted")
        }
    }

}