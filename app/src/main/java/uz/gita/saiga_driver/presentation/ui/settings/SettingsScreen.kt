package uz.gita.saiga_driver.presentation.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.MainActivity
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.databinding.ScreenSettingsBinding
import uz.gita.saiga_driver.di.DatabaseModule
import uz.gita.saiga_driver.presentation.dialogs.ChooseLanguageDialog
import uz.gita.saiga_driver.presentation.dialogs.MapTypeDialog
import uz.gita.saiga_driver.utils.extensions.include
import javax.inject.Inject

// Created by Jamshid Isoqov on 12/13/2022
@AndroidEntryPoint
class SettingsScreen : Fragment(R.layout.screen_settings) {

    @Inject
    lateinit var mySharedPref: MySharedPref

    private val viewBinding: ScreenSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {


        setMapType(mySharedPref.mapType)

        imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        tvLanguage.text = when (mySharedPref.language) {
            0 -> "Uzbek"
            1 -> "English"
            else -> "Karakalpak"
        }

        containerLan.setOnClickListener {
            val dialog = ChooseLanguageDialog()
            dialog.setChangeLanguageListener {
                MainActivity.activity.setNewLocale()
            }
            dialog.show(childFragmentManager, "language")
        }

        cardLogOut.setOnClickListener {
            mySharedPref.isVerify = false
            findNavController().navigate(SettingsScreenDirections.actionSettingsScreenToLoginScreen())
        }

        containerMapType.setOnClickListener {
            val dialog = MapTypeDialog()
            dialog.setChangeMapListener {
                setMapType(it)
            }
            dialog.show(childFragmentManager,"map_type")
        }
    }

    private fun setMapType(type:Int){
        viewBinding.tvMapType.text = when(type){
            0->resources.getString(R.string.normal)
            1->resources.getString(R.string.satellite)
            else->resources.getString(R.string.hybrid)
        }
    }

}