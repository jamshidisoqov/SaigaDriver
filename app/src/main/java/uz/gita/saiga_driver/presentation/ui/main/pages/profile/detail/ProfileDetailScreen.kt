package uz.gita.saiga_driver.presentation.ui.main.pages.profile.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenProfileDetailBinding
import uz.gita.saiga_driver.presentation.dialogs.ChooseDateDialog
import uz.gita.saiga_driver.presentation.presenter.ProfileDetailViewModelImpl
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/17/2022
@AndroidEntryPoint
class ProfileDetailScreen : Fragment(R.layout.screen_profile_detail) {

    private val viewBinding: ScreenProfileDetailBinding by viewBinding()

    private val viewModel: ProfileDetailViewModel by viewModels<ProfileDetailViewModelImpl>()

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

        viewModel.firstNameFlow.onEach {
            inputFirstName.setText(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.lastNameFlow.onEach {
            inputLastName.setText(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.phoneFlow.onEach {
            inputPhone.text = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.birthdayFlow.onEach {
            inputBirthday.text = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        inputBirthday.setOnClickListener {
            val dialog = ChooseDateDialog(
                requireContext(),
                viewBinding.inputBirthday.text.toString().toDate()
            )
            dialog.setConfirmClickListener {
                viewBinding.inputBirthday.text = getCurrentDate(it)
            }
            dialog.show()
        }

        btnSave.setOnClickListener {
            viewModel.saveData(
                firstName = inputFirstName.text.toString(),
                lastName = inputLastName.text.toString(),
                birthDay = inputBirthday.text.toString()
            )
        }

        viewModel.getData()
    }

}