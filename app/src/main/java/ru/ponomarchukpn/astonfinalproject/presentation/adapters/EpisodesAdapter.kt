package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity

class EpisodesAdapter(
    private val onListEnded: (() -> Unit)?,
    private val onItemClick: (EpisodeEntity) -> Unit
) : ListAdapter<EpisodeEntity, EpisodesAdapter.EpisodesViewHolder>(
    EpisodesDiffCallback
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_episode, parent, false)
            .let {
                EpisodesViewHolder(it)
            }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val entity = getItem(position)
        holder.name.text = entity.name
        holder.code.text = entity.episode
        holder.airDate.text = entity.airDate

        holder.itemView.setOnClickListener {
            onItemClick.invoke(entity)
        }
        if (position == currentList.size - 1) {
            onListEnded?.invoke()
        }
    }

    class EpisodesViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_episode_name)
        val code: TextView = itemView.findViewById(R.id.item_episode_code)
        val airDate: TextView = itemView.findViewById(R.id.item_episode_air_date)
    }
}
