package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity

object LocationsDiffCallback : DiffUtil.ItemCallback<LocationEntity>() {

    override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
        return oldItem == newItem
    }
}
