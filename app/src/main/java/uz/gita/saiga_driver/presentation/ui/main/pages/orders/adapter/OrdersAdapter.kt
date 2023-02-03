package uz.gita.saiga_driver.presentation.ui.main.pages.orders.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ListItemOrdersBinding
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.inflate

// Created by Jamshid Isoqov on 12/19/2022

private var listItemCallback = object : DiffUtil.ItemCallback<OrderData>() {
    override fun areItemsTheSame(oldItem: OrderData, newItem: OrderData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OrderData, newItem: OrderData): Boolean =
        oldItem.id == newItem.id && oldItem.driver == newItem.driver &&
                oldItem.createdDate == newItem.createdDate &&
                oldItem.updatedDate == newItem.updatedDate &&
                oldItem.direction == newItem.direction

}

class OrdersAdapter : ListAdapter<OrderData, OrdersAdapter.ViewHolder>(listItemCallback) {

    private var itemClickListener: ((OrderData) -> Unit)? = null

    fun setItemClickListener(block: (OrderData) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(private val binding: ListItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun onBind() = binding.include {
            val data = getItem(absoluteAdapterPosition)
            tvFromOrder.text = data.direction.addressFrom.title
            tvToOrder.text = data.direction.addressTo.title
            tvCarType.text = data.driver.car?.model?.name ?: "Light weight"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemOrdersBinding.bind(parent.inflate(R.layout.list_item_orders)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()


}