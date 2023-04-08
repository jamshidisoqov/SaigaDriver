package uz.gita.saiga_driver.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.DialogCheckConnectionBinding
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.include

class CheckConnectionDialog : DialogFragment(R.layout.dialog_check_connection) {

    private val viewBinding: DialogCheckConnectionBinding by viewBinding(
        DialogCheckConnectionBinding::bind
    )

    private var onCheckListener: (() -> Unit)? = null

    fun setOnCheckListener(block: () -> Unit) {
        onCheckListener = block
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        btnCheck.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                onCheckListener?.invoke()
                dismiss()
            }.launchIn(lifecycleScope)
    }

}