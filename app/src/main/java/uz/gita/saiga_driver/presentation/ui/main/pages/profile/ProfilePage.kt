package uz.gita.saiga_driver.presentation.ui.main.pages.profile

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
import uz.gita.saiga_driver.databinding.PageProfileBinding
import uz.gita.saiga_driver.presentation.presenter.ProfileViewModelImpl
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class ProfilePage : Fragment(R.layout.page_profile) {

    private val viewModel: ProfileViewModel by viewModels<ProfileViewModelImpl>()

    private val viewBinding: PageProfileBinding by viewBinding()

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

        viewModel.nameFlow.onEach {
            tvUserName.text = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.phoneFlow.onEach {
            tvPhoneNumber.text = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        containerDirections.setOnClickListener {
            viewModel.navigateToDirections()
        }

        containerFinance.setOnClickListener {
            viewModel.navigateToFinance()
        }

        containerService.setOnClickListener {
            viewModel.navigateToCustomerCare()
        }
        containerSettings.setOnClickListener {
            viewModel.navigateToSettings()
        }

        viewModel.getData()
    }

}