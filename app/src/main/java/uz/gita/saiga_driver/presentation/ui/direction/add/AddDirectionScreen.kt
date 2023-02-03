package uz.gita.saiga_driver.presentation.ui.direction.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.databinding.ScreenAddDirectionBinding
import uz.gita.saiga_driver.presentation.presenter.AddDirectionViewModelImpl
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/13/2022
@AndroidEntryPoint
class AddDirectionScreen : Fragment(R.layout.screen_add_direction) {

    private val viewModel: AddDirectionViewModel by viewModels<AddDirectionViewModelImpl>()

    private val viewBinding: ScreenAddDirectionBinding by viewBinding()

    private val boolFrom: Boolean = false
    private val boolTo: Boolean = false
    private val boolPrice: Boolean = false
    private val boolSchedule: Boolean = false

    private var allDirection = emptyList<DirectionResponse>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        viewModel.allDirections.onEach {
            allDirection = it
        }.launchIn(lifecycleScope)

        viewModel.loadingSharedFlow.onEach {
            if (it) showProgress() else hideProgress()
        }.launchIn(lifecycleScope)

        viewModel.errorSharedFlow.onEach {
            showErrorDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow.onEach {
            showMessageDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.backFlow.onEach {
            findNavController().navigateUp()
        }.launchIn(lifecycleScope)

    }

}