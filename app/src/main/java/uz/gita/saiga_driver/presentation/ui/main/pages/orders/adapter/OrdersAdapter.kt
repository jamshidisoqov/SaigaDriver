package uz.gita.saiga_driver.presentation.ui.main.pages.orders.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.databinding.ListItemOrdersBinding
import uz.gita.saiga_driver.utils.extensions.combine
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.inflate

// Created by Jamshid Isoqov on 12/19/2022

private var listItemCallback = object : DiffUtil.ItemCallback<OrderResponse>() {
    override fun areItemsTheSame(oldItem: OrderResponse, newItem: OrderResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderResponse, newItem: OrderResponse): Boolean =
        oldItem.id == newItem.id && oldItem.direction == newItem.direction
                && oldItem.fromUser == newItem.fromUser && oldItem.money == newItem.money


}

class OrdersAdapter :
    ListAdapter<OrderResponse, OrdersAdapter.ViewHolder>(listItemCallback) {


    private var itemClickListener: ((OrderResponse) -> Unit)? = null

    fun setItemClickListener(block: (OrderResponse) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(private val binding: ListItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind() = binding.include {
            val data = getItem(absoluteAdapterPosition)
            tvFromOrder.text = data.direction.addressFrom.title
            tvToOrder.text = data.direction.addressTo?.title ?: "Not specified"
            tvDistance.text = data.distance.combine("km")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemOrdersBinding.bind(parent.inflate(R.layout.list_item_orders)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()


}