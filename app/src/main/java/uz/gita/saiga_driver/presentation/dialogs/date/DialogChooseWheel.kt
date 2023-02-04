package uz.gita.saiga_driver.presentation.dialogs.date

import android.graphics.Color
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.DialogChooseDateWheelBinding
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 2/4/2023
class DialogChooseWheel : BottomSheetDialogFragment(R.layout.dialog_choose_date_wheel) {

    private val viewBinding: DialogChooseDateWheelBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        wheelMonth.apply {
            this.isCurved = true
            isCyclic = true
            setAtmospheric(true)
            setCurtain(true)
            data = listOf("March", "May", "April")
            curtainColor = Color.parseColor("#86F8D0AD")
            itemTextColor = Color.parseColor("#FC9E4C")
            itemSpace = 20
        }

        wheelDate.apply {
            this.isCurved = true
            isCyclic = true
            setCurtain(true)
            curtainColor = Color.parseColor("#86F8D0AD")
            itemTextColor = Color.parseColor("#FC9E4C")
            setAtmospheric(true)
        }

        wheelYear.apply {
            this.isCurved = true
            isCyclic = true
            setCurtain(true)
            curtainColor = Color.parseColor("#86F8D0AD")
            itemTextColor = Color.parseColor("#FC9E4C")
            setAtmospheric(true)
            yearStart = 2023
        }
    }
}