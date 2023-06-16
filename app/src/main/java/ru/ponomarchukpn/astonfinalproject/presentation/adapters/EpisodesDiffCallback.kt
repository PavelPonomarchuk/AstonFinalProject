package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity

object EpisodesDiffCallback : DiffUtil.ItemCallback<EpisodeEntity>() {

    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem == newItem
    }
}
