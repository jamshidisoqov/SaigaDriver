package uz.gita.saiga_driver.presentation.ui.permission.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.saiga_driver.presentation.ui.permission.pages.LocationPage
import uz.gita.saiga_driver.presentation.ui.permission.pages.NotificationPage

// Created by Jamshid Isoqov on 12/18/2022
class PermissionsAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> LocationPage()
            else -> NotificationPage()
        }
}