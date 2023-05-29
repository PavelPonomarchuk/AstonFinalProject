package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity

object CharactersDiffCallback : DiffUtil.ItemCallback<CharacterEntity>() {

    override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem == newItem
    }
}
