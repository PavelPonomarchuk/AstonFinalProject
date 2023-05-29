package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity

class LocationsAdapter(
    private val onListEnded: () -> Unit,
    private val onItemClick: (LocationEntity) -> Unit
) : ListAdapter<LocationEntity, LocationsAdapter.LocationsViewHolder>(
    LocationsDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
            .let {
                LocationsViewHolder(it)
            }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        val entity = getItem(position)
        holder.name.text = entity.name
        holder.type.text = entity.type
        holder.dimension.text = entity.dimension

        holder.itemView.setOnClickListener {
            onItemClick.invoke(entity)
        }
        if (position == currentList.size - 1) {
            onListEnded.invoke()
        }
    }

    class LocationsViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_location_name)
        val type: TextView = itemView.findViewById(R.id.item_location_type)
        val dimension: TextView = itemView.findViewById(R.id.item_location_dimension)
    }
}
