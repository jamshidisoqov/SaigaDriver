package uz.gita.saiga_driver.presentation.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenRegisterBinding
import uz.gita.saiga_driver.presentation.presenter.RegisterViewModelImpl
import uz.gita.saiga_driver.utils.DEBOUNCE_TEXT_CHANGES
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()

    private val viewBinding: ScreenRegisterBinding by viewBinding()

    private var boolPhone: Boolean = false

    private var boolFirstName: Boolean = false

    private var boolLastName: Boolean = false

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        viewModel.loadingSharedFlow.onEach {
            if (it) showProgress() else hideProgress()
        }.launchIn(lifecycleScope)

        viewModel.errorSharedFlow.onEach {
            showErrorDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow.onEach {
            showMessageDialog(it)
        }.launchIn(lifecycleScope)

        inputPhone.textChanges()
            .debounce(DEBOUNCE_TEXT_CHANGES)
            .onEach {
                boolPhone = it.toString().length == 17
                check()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        inputName.textChanges()
            .debounce(DEBOUNCE_TEXT_CHANGES)
            .onEach {
                boolFirstName = it.toString().length >= 3
                check()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        inputLastName.textChanges()
            .debounce(DEBOUNCE_TEXT_CHANGES)
            .onEach {
                boolLastName = it.toString().length >= 3
                check()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        btnRegister.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.register(
                    phone = inputPhone.text.toString(),
                    firstName = inputName.text.toString(),
                    lastName = inputLastName.text.toString()
                )
            }.launchIn(lifecycleScope)

    }

    private fun check() = viewBinding.include {
        btnRegister.isEnabled = boolPhone && boolLastName && boolFirstName
    }
}