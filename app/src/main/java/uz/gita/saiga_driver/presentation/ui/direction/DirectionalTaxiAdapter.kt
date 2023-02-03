package uz.gita.saiga_driver.presentation.ui.direction

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ListItemDirectionsBinding
import uz.gita.saiga_driver.utils.extensions.getFinanceType
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.inflate

// Created by Jamshid Isoqov on 12/17/2022

val directionItemCallback = object : DiffUtil.ItemCallback<DirectionalTaxiData>() {
    override fun areItemsTheSame(
        oldItem: DirectionalTaxiData,
        newItem: DirectionalTaxiData
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: DirectionalTaxiData,
        newItem: DirectionalTaxiData
    ): Boolean = oldItem.directionsData == newItem.directionsData &&
            oldItem.price == newItem.price &&
            oldItem.scheduleTime == newItem.scheduleTime

}

class DirectionalTaxiAdapter :
    ListAdapter<DirectionalTaxiData, DirectionalTaxiAdapter.ViewHolder>(directionItemCallback) {

    private var itemClickListener: ((DirectionalTaxiData) -> Unit)? = null

    fun setItemClickListener(block: (DirectionalTaxiData) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(private val binding: ListItemDirectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun onBind() = binding.include {
            val data = getItem(absoluteAdapterPosition)
            tvFirstAddress.text = data.directionsData.addressFrom.title
            tvSecondAddress.text = data.directionsData.addressTo.title
            tvTime.text = data.scheduleTime.toString()
            tvMoney.text = data.price.getFinanceType()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemDirectionsBinding.bind(parent.inflate(R.layout.list_item_directions)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

}