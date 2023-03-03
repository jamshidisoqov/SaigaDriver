package uz.gita.saiga_driver.presentation.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.databinding.DialogLanguageBinding
import uz.gita.saiga_driver.utils.extensions.inVisible
import uz.gita.saiga_driver.utils.extensions.visible
import javax.inject.Inject

// Created by Jamshid Isoqov an 10/25/2022
@AndroidEntryPoint
class ChooseLanguageDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogLanguageBinding

    private var changeLanguageListener: (() -> Unit)? = null

    fun setChangeLanguageListener(block: () -> Unit) {
        changeLanguageListener = block
    }

    @Inject
    lateinit var mySharedPref: MySharedPref

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogLanguageBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        checkLanguage()
        binding.containerUzbekLng.setOnClickListener {
            mySharedPref.language = 0
            changeLanguageListener?.invoke()
        }
        binding.containerEnglishLng.setOnClickListener {
            mySharedPref.language = 1
            changeLanguageListener?.invoke()
        }

        binding.containerKaaLng.setOnClickListener {
            mySharedPref.language = 2
            changeLanguageListener?.invoke()
        }

        return binding.root
    }

    private fun checkLanguage() {
        val lan = mySharedPref.language
        binding.apply {
            when (lan) {
                0 -> {
                    imageKaaCheck.inVisible()
                    imageUzbCheck.visible()
                    imageEngCheck.inVisible()
                }
                1 -> {
                    imageKaaCheck.inVisible()
                    imageUzbCheck.inVisible()
                    imageEngCheck.visible()
                }
                else -> {
                    imageKaaCheck.visible()
                    imageUzbCheck.inVisible()
                    imageEngCheck.inVisible()
                }
            }
        }
    }
}