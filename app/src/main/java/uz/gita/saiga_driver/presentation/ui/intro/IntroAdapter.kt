package uz.gita.saiga_driver.presentation.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.saiga_driver.presentation.ui.intro.pages.IntroPage

// Created by Jamshid Isoqov on 3/6/2023
class IntroAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val args = Bundle()
        args.putInt("position", position)
        return with(IntroPage()) {
            arguments = args
            this
        }
    }
}