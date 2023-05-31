package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity

class CharactersAdapter : ListAdapter<CharacterEntity, CharactersAdapter.CharactersViewHolder>(
    CharactersDiffCallback
) {

    var onListEnded: (() -> Unit)? = null
    var onCharacterClick: ((CharacterEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
            .let {
                CharactersViewHolder(it)
            }
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val characterEntity = getItem(position)
        holder.name.text = characterEntity.name
        holder.itemView.setOnClickListener {
            onCharacterClick?.invoke(characterEntity)
        }

        if (position == currentList.size - 1) {
            onListEnded?.invoke()
        }

    }

    class CharactersViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.character_name)
    }
}
