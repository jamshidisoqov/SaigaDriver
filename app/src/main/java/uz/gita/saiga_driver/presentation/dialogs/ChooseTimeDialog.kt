package uz.gita.saiga_driver.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import uz.gita.saiga_driver.databinding.DialogChooseTimeBinding
import uz.gita.saiga_driver.utils.extensions.toTime

// Created by Jamshid Isoqov an 8/9/2022
class ChooseTimeDialog(ctx: Context, private val time: String) : Dialog(ctx) {

    private lateinit var binding: DialogChooseTimeBinding

    private var timeListener: ((String) -> Unit)? = null

    fun setTimeListener(block: (String) -> Unit) {
        timeListener = block
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogChooseTimeBinding.inflate(layoutInflater)
        binding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            spinnerTime.hour = time.toTime()[0].toInt()
            spinnerTime.minute = time.toTime()[1].toInt()
            btnSave.setOnClickListener {
                val hour = binding.spinnerTime.hour
                val minute = binding.spinnerTime.minute
                val hourString = if (hour < 10) "0$hour" else hour
                val minuteString = if (minute < 10) "0$minute" else minute
                timeListener?.invoke("$hourString:$minuteString")
                dismiss()
            }
        }
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}