package uz.gita.saiga_driver.presentation.ui.finance

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenFinanceBinding
import uz.gita.saiga_driver.presentation.presenter.FinanceViewModelImpl
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/13/2022

@AndroidEntryPoint
class FinanceScreen : Fragment(R.layout.screen_finance) {

    private val viewModel: FinanceViewModel by viewModels<FinanceViewModelImpl>()

    private val viewBinding: ScreenFinanceBinding by viewBinding()

    private val adapter: TripHistoryAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TripHistoryAdapter()
    }

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

        imageBack.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                findNavController().navigateUp()
            }.launchIn(lifecycleScope)

        listHistory.adapter = adapter

        viewModel.allOrdersHistory.onEach {
            if (it.isEmpty()) {
                imagePlaceHolder.visible()
            } else {
                imagePlaceHolder.gone()
            }
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getAllTrips()
    }

}