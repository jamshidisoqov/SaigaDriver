package uz.gita.saiga_driver.presentation.ui.intro.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.PageIntroBinding
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 3/6/2023
class IntroPage : Fragment(R.layout.page_intro) {

    private val viewBinding: PageIntroBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        arguments?.apply {
            val pos = this.getInt("position", 0)
            imageIntroPage.setImageResource(
                when (pos) {
                    0 -> R.drawable.on_boarding_icon1
                    1 -> R.drawable.on_boarding_icon2
                    else -> R.drawable.on_boarding_icon3
                }
            )
            tvTitle.text = resources.getStringArray(R.array.title_intro)[pos]
            tvSubtitle.text = resources.getStringArray(R.array.sub_title_intro)[pos]
        }
    }
}