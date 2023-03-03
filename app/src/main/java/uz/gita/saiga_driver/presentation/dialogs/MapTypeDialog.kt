package uz.gita.saiga_driver.presentation.dialogs

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.databinding.DialogMapTypeBinding
import uz.gita.saiga_driver.utils.extensions.include
import javax.inject.Inject

// Created by Jamshid Isoqov on 3/3/2023
@AndroidEntryPoint
class MapTypeDialog : BottomSheetDialogFragment(R.layout.dialog_map_type) {

    @Inject
    lateinit var mySharedPref: MySharedPref

    private var changeMapListener: ((Int) -> Unit)? = null

    fun setChangeMapListener(block: (Int) -> Unit) {
        changeMapListener = block
    }

    private val viewBinding: DialogMapTypeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        val select = mySharedPref.mapType

        for (i in 1..5 step 2) {
            val index = i / 2
            root.getChildAt(i).apply {
                setOnClickListener {
                    mySharedPref.mapType = index
                    changeMapListener?.invoke(index)
                    dismiss()
                }
                if (select == index) {
                    setBackgroundResource(R.drawable.bg_stroke)
                }
            }
        }
    }
}