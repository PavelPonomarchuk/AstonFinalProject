package ru.ponomarchukpn.astonfinalproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus

class CharactersAdapter(
    private val onListEnded: (() -> Unit)?,
    private val onItemClick: (CharacterEntity) -> Unit
) : ListAdapter<CharacterEntity, CharactersAdapter.CharactersViewHolder>(
    CharactersDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
            .let {
                CharactersViewHolder(it)
            }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val entity = getItem(position)
        holder.name.text = entity.name
        holder.species.text = entity.species
        //TODO вынести строки
        holder.status.text = when (entity.status) {
            CharacterStatus.ALIVE -> "Alive"
            CharacterStatus.DEAD -> "Dead"
            CharacterStatus.UNKNOWN -> "Unknown"
        }
        holder.gender.text = when (entity.gender) {
            CharacterGender.FEMALE -> "Female"
            CharacterGender.MALE -> "Male"
            CharacterGender.GENDERLESS -> "Genderless"
            CharacterGender.UNKNOWN -> "Unknown"
        }
        //TODO плейсхолдер можно сделать на случай ошибки
        holder.image.load(entity.imageUrl)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(entity)
        }
        if (position == currentList.size - 1) {
            onListEnded?.invoke()
        }
    }

    class CharactersViewHolder(itemView: View) : ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.item_character_image)
        val name: TextView = itemView.findViewById(R.id.item_character_name)
        val species: TextView = itemView.findViewById(R.id.item_character_species)
        val status: TextView = itemView.findViewById(R.id.item_character_status)
        val gender: TextView = itemView.findViewById(R.id.item_character_gender)
    }
}
