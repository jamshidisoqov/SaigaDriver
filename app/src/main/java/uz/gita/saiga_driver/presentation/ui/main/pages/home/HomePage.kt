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
import uz.gita.saiga_driver.utils.driverStatusLiveData
import uz.gita.saiga_driver.utils.extensions.*

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val viewBinding: PageHomeBinding by viewBinding()

    private val viewModel: HomeViewModel by viewModels<HomeViewModelImpl>()

    private val adapter: DirectionalTaxiAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DirectionalTaxiAdapter()
    }

    private var navigateToOrder: ((Int) -> Unit)? = null

    fun setNavigateToOrder(block: (Int) -> Unit) {
        navigateToOrder = block
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        listDirections.adapter = adapter

        driverStatusLiveData.value = btnSocketState.isChecked

        //observers
        viewModel.loadingSharedFlow.onEach {
            if (it) {
                showProgress()
                containerDirectionsShimmer.apply {
                    startShimmer()
                    visible()
                }
                listDirections.inVisible()
            } else {
                hideProgress()
                containerDirectionsShimmer.apply {
                    stopShimmer()
                    gone()
                }
                listDirections.visible()
            }
        }.launchIn(lifecycleScope)

        viewModel.errorSharedFlow
            .onEach {
                showErrorDialog(it)
            }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow
            .onEach {
                showMessageDialog(it)
            }.launchIn(lifecycleScope)

        viewModel.currentBalanceFlow.observe(viewLifecycleOwner) {
            tvTotalBalance.text = it.getFormat(3).combine("sum")
        }

        viewModel.loadingBalanceLiveData.observe(viewLifecycleOwner) {
            if (it) {
                shimmerBalance.apply {
                    startShimmer()
                    visible()
                }
                containerBalance.gone()
            } else {
                shimmerBalance.apply {
                    stopShimmer()
                    inVisible()
                }
                containerBalance.visible()
            }
        }

        viewModel.loadingOrdersLiveData.observe(viewLifecycleOwner) {
            if (it) {
                containerShimmerOrders.apply {
                    startShimmer()
                    visible()
                }
                containerOrders.gone()
            } else {
                containerShimmerOrders.apply {
                    stopShimmer()
                    inVisible()
                }
                containerOrders.visible()
            }
        }

        viewModel.loadingFinanceLiveData.observe(viewLifecycleOwner) {
            if (it) {
                containerShimmerIncome.apply {
                    startShimmer()
                    visible()
                }
                containerShimmerExpanses.apply {
                    startShimmer()
                    visible()
                }
                containerIncome.gone()
                containerExpanses.gone()
            } else {
                containerShimmerIncome.apply {
                    stopShimmer()
                    inVisible()
                }
                containerShimmerExpanses.apply {
                    stopShimmer()
                    inVisible()
                }
                containerIncome.visible()
                containerExpanses.visible()
            }
        }

        viewModel.expanseBalance.onEach {
            tvOrderExpanse.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.incomeBalance.onEach {
            tvOrderIncome.text = it.getFinanceType()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.ordersFlow.observe(viewLifecycleOwner) {
            tvOrderCount.text = it.toString().combine("Orders")
        }

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

        viewModel.refreshUserBalance()

        //events
        imageNotification.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                viewModel.navigateToNotifications()
            }.launchIn(lifecycleScope)

        cardOrderContainer.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                navigateToOrder?.invoke(1)
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

        btnSocketState.setOnCheckedChangeListener { _, bool ->
            driverStatusLiveData.value = bool
            if (bool) {
                viewModel.getAllOrders()
                containerEnabledOrders.gone()
            } else {
                containerEnabledOrders.visible()
            }
        }

        btnEnabled.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                btnSocketState.isChecked = true
            }.launchIn(lifecycleScope)

        btnSocketState.isChecked = true

        imageRefreshMoney.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                refreshAnim(imageRefreshMoney)
                viewModel.refreshUserBalance()
            }.launchIn(lifecycleScope)

        imageRefreshOrders.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                refreshAnim(imageRefreshOrders)
                viewModel.getAllOrders()
            }.launchIn(lifecycleScope)

        imageRefreshExpanses.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                refreshAnim(imageRefreshExpanses)
                viewModel.getDriverFinance()
            }.launchIn(lifecycleScope)

        imageRefreshIncome.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                refreshAnim(imageRefreshIncome)
                viewModel.getDriverFinance()
            }.launchIn(lifecycleScope)
    }

    private fun refreshAnim(view: View) {
        view.animate().apply {
            duration = 2000
            this.rotation(720f)
        }.withEndAction {
            view.rotation = 0f
        }.start()
    }
}