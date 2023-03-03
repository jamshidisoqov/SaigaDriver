package uz.gita.saiga_driver.presentation.ui.main.pages.orders

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.databinding.PageOrdersBinding
import uz.gita.saiga_driver.presentation.dialogs.NearAddressDialog
import uz.gita.saiga_driver.presentation.presenter.OrdersViewModelImpl
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.adapter.OrdersAdapter
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.dialog.OrderDialog
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.GpsService
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.hasPermission
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.showErrorDialog
import uz.gita.saiga_driver.utils.extensions.showMessageDialog

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class OrdersPage : Fragment(R.layout.page_orders) {

    val viewModel: OrdersViewModel by viewModels<OrdersViewModelImpl>()

    private val viewBinding: PageOrdersBinding by viewBinding()

    private val adapter: OrdersAdapter by lazy(LazyThreadSafetyMode.NONE) {
        OrdersAdapter()
    }

    private var isShown = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        listOrders.adapter = adapter

        viewModel.errorSharedFlow.onEach {
            showErrorDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow.onEach {
            showMessageDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.allOrderFlow.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentLocationFlow.observe(viewLifecycleOwner) {}

        viewModel.openOrderDialog.onEach {
            showNearDialog(it)
        }.launchIn(lifecycleScope)

        root.setOnRefreshListener {
            lifecycleScope.launch {
                delay(2000)
                root.isRefreshing = false
            }
        }

        viewModel.getAllData()

        startGps()

        adapter.setItemClickListener {
            val dialog = OrderDialog(it)
            dialog.setAcceptListener { order ->
                viewModel.accept(order.copy(distance = ""))
            }
            dialog.show(childFragmentManager, "order")
        }
    }

    private fun showNearDialog(orderResponse: OrderResponse) {
        if (!isShown) {
            isShown = true
            val dialog = NearAddressDialog(orderResponse)
            dialog.setAcceptListener { order ->
                viewModel.accept(order.copy(distance = ""))
            }
            dialog.dialog?.setOnDismissListener {
                isShown = false
            }
            dialog.show(childFragmentManager, "showNearDialog")
        }
    }

    private fun startGps() {
        hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, onPermissionGranted = {
            val intent = Intent(requireContext(), GpsService::class.java)
            requireContext().startService(intent)
            currentLocation.observe(viewLifecycleOwner) {
                viewModel.setCurrentLocation(it)
            }
        }) {}

    }

    override fun onResume() {
        super.onResume()
        viewModel.setCurrentLocation(currentLocation.value ?: NUKUS)
    }
}