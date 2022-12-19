package uz.gita.saiga_driver.presentation.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.saiga_driver.presentation.ui.main.pages.home.HomePage
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.OrdersPage
import uz.gita.saiga_driver.presentation.ui.main.pages.profile.ProfilePage

// Created by Jamshid Isoqov on 12/19/2022
class MainAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> HomePage()
            1 -> OrdersPage()
            else -> ProfilePage()
        }


}