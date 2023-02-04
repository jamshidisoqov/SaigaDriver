package uz.gita.saiga_driver.presentation.ui.permission.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.PageLocationBinding
import uz.gita.saiga_driver.utils.LOCATION_PERMISSION
import uz.gita.saiga_driver.utils.PageEventListener
import uz.gita.saiga_driver.utils.extensions.hasPermission

// Created by Jamshid Isoqov on 12/12/2022
class LocationPage : Fragment(R.layout.page_location) {

    private val viewBinding: PageLocationBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkPermission()
    }
    private fun checkPermission() {
        hasPermission(
            permission = LOCATION_PERMISSION,
            onPermissionGranted = { PageEventListener.pageListener.value = 1 }) {
            Snackbar.make(
                viewBinding.imageLocation, "Permission denied",
                Snackbar.LENGTH_SHORT
            ).setAction("Try check") { checkPermission() }
                .show()
        }
    }
}