package uz.gita.saiga_driver.presentation.ui.main.pages.home

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
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.PageHomeBinding
import uz.gita.saiga_driver.presentation.presenter.HomeViewModelImpl
import uz.gita.saiga_driver.presentation.ui.direction.DirectionalTaxiAdapter
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val viewBinding: PageHomeBinding by viewBinding()

    private val viewModel: HomeViewModel by viewModels<HomeViewModelImpl>()

    private val adapter: DirectionalTaxiAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DirectionalTaxiAdapter()
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        listDirections.adapter = adapter

        //observers
        viewModel.loadingSharedFlow.onEach {
            if (it) showProgress() else hideProgress()
        }.launchIn(lifecycleScope)

        viewModel.errorSharedFlow
            .onEach {
                showErrorDialog(it)
            }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow
            .onEach {
                showMessageDialog(it)
            }.launchIn(lifecycleScope)

        viewModel.currentBalanceFlow.onEach {
            tvTotalBalance.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.expanseBalance.onEach {
            tvOrderExpanse.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.incomeBalance.onEach {
            tvOrderIncome.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.ordersFlow.onEach {
            tvOrderCount.text = it.toString().combine("Orders")
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.myDirectionsFlow.onEach {
            if (it.isEmpty()) {
                addDirectionContainer.visible()
            } else addDirectionContainer.gone()
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.nameSharedFlow.onEach {
            tvTitleHome.text = getStringResource(R.string.hello).combine(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getData()

        viewModel.getAllMyDirections()

        //events
        imageNotification.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToNotifications()
            }.launchIn(lifecycleScope)

        cardOrderContainer.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToOrders()
            }.launchIn(lifecycleScope)

        cardIncomeContainer.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToFinance()
            }.launchIn(lifecycleScope)

        cardExpanseContainer.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToFinance()
            }.launchIn(lifecycleScope)

        addDirectionContainer.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToAddDirection()
            }.launchIn(lifecycleScope)

        adapter.setItemClickListener {
            viewModel.navigateToDirectionDetail(it)
        }

        imageRefreshMoney.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                imageRefreshMoney.animate().apply {
                    duration = 2000
                    this.rotation(720f)
                }.start()
                viewModel.refreshUserBalance()
            }.launchIn(lifecycleScope)
    }
}