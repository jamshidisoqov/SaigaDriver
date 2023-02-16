package uz.gita.saiga_driver.presentation.ui.finance

// Created by Jamshid Isoqov on 2/6/2023

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.databinding.ListItemHistoryBinding
import uz.gita.saiga_driver.databinding.ListItemTripsHeaderBinding
import uz.gita.saiga_driver.domain.entity.TripDate
import uz.gita.saiga_driver.domain.entity.TripWithDate
import uz.gita.saiga_driver.utils.extensions.getFinanceType
import uz.gita.saiga_driver.utils.extensions.inflate

// Created by Jamshid Isoqov on 11/29/2022

private val tripItemCallback = object : DiffUtil.ItemCallback<TripWithDate>() {
    override fun areItemsTheSame(oldItem: TripWithDate, newItem: TripWithDate): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TripWithDate, newItem: TripWithDate): Boolean {
        return if (oldItem is OrderResponse && newItem is OrderResponse) {
            oldItem.id == newItem.id && oldItem.fromUser == newItem.fromUser
        } else if (oldItem is TripDate && newItem is TripDate) {
            oldItem.tripDate == newItem.tripDate
        } else false
    }

}

class TripHistoryAdapter :
    ListAdapter<TripWithDate, BaseViewHolder>(tripItemCallback) {

    private var itemClickListener: ((OrderResponse) -> Unit)? = null

    fun setItemClickListener(block: (OrderResponse) -> Unit) {
        itemClickListener = block
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == 1) {
            ViewHolderTrips(ListItemHistoryBinding.bind(parent.inflate(R.layout.list_item_history)))
        } else {
            return ViewHolderTripDate(ListItemTripsHeaderBinding.bind(parent.inflate(R.layout.list_item_trips_header)))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(getItem(position)) {
            itemClickListener?.invoke(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is OrderResponse -> 1
            else -> 2
        }
    }
}


abstract class BaseViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun <T> onBind(data: T, block: (OrderResponse) -> Unit)
}

class ViewHolderTrips(private val binding: ListItemHistoryBinding) : BaseViewHolder(binding) {

    override fun <T> onBind(data: T, block: (OrderResponse) -> Unit) {
        val trip = data as OrderResponse
        binding.apply {
            tvFromOrder.text = trip.direction.addressFrom.title
            tvToOrder.text = trip.direction.addressTo?.title ?: "Not specific"
            tvMoney.text = trip.money.getFinanceType()
        }
    }

}

class ViewHolderTripDate(private val binding: ListItemTripsHeaderBinding) :
    BaseViewHolder(binding) {

    override fun <T> onBind(data: T, block: (OrderResponse) -> Unit) {
        val date = data as TripDate
        binding.apply {
            tvDate.text = date.tripDate
        }
    }
}