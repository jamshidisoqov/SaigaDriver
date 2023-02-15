package uz.gita.saiga_driver.presentation.ui.direction

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.databinding.ListItemDirectionsBinding
import uz.gita.saiga_driver.utils.extensions.getFinanceType
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.inflate

// Created by Jamshid Isoqov on 12/17/2022

val directionItemCallback = object : DiffUtil.ItemCallback<OrderResponse>() {
    override fun areItemsTheSame(
        oldItem: OrderResponse,
        newItem: OrderResponse
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: OrderResponse,
        newItem: OrderResponse
    ): Boolean = oldItem.id == newItem.id

}

class DirectionalTaxiAdapter :
    ListAdapter<OrderResponse, DirectionalTaxiAdapter.ViewHolder>(directionItemCallback) {

    private var itemClickListener: ((OrderResponse) -> Unit)? = null

    fun setItemClickListener(block: (OrderResponse) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(private val binding: ListItemDirectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tripCardView.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun onBind() = binding.include {
            val data = getItem(absoluteAdapterPosition)
            tvFirstAddress.text = data.direction.addressFrom.title
            tvSecondAddress.text = data.direction.addressTo?.title ?: "Not Specified"
            tvTime.text = data.timeWhen.toString()
            tvMoney.text = data.money.getFinanceType()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemDirectionsBinding.bind(parent.inflate(R.layout.list_item_directions)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

}