package uz.gita.saiga_driver.presentation.ui.direction.add.address

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse
import uz.gita.saiga_driver.databinding.ListItemOrderMapBinding
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.inflate

// Created by Jamshid Isoqov on 2/24/2023

private val listItemOrderMapCallback = object : DiffUtil.ItemCallback<AddressResponse>() {
    override fun areItemsTheSame(oldItem: AddressResponse, newItem: AddressResponse): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AddressResponse, newItem: AddressResponse): Boolean =
        oldItem.lat == newItem.lat && oldItem.lon == newItem.lon && oldItem.distance == newItem.distance

}

class AddressAdapter :
    ListAdapter<AddressResponse, AddressAdapter.ViewHolder>(listItemOrderMapCallback) {

    private var itemClickListener: ((AddressResponse) -> Unit)? = null

    fun setItemClickListener(block: (AddressResponse) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(private val binding: ListItemOrderMapBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun onBind() = binding.include {
            val data = getItem(absoluteAdapterPosition)
            tvAddressName.text = data.title
            tvAddressDistance.text = data.distance
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemOrderMapBinding.bind(parent.inflate(R.layout.list_item_order_map)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

}