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

        holder.status.text = when (entity.status) {
            CharacterStatus.ALIVE -> STATUS_ALIVE
            CharacterStatus.DEAD -> STATUS_DEAD
            CharacterStatus.UNKNOWN -> STATUS_UNKNOWN
        }
        holder.gender.text = when (entity.gender) {
            CharacterGender.FEMALE -> GENDER_FEMALE
            CharacterGender.MALE -> GENDER_MALE
            CharacterGender.GENDERLESS -> GENDER_GENDERLESS
            CharacterGender.UNKNOWN -> GENDER_UNKNOWN
        }
        holder.image.load(entity.imageUrl) {
            error(R.drawable.placeholder_avatar)
        }
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

    companion object {

        private const val STATUS_ALIVE = "Alive"
        private const val STATUS_DEAD = "Dead"
        private const val STATUS_UNKNOWN = "Unknown"

        private const val GENDER_FEMALE = "Female"
        private const val GENDER_MALE = "Male"
        private const val GENDER_GENDERLESS = "Genderless"
        private const val GENDER_UNKNOWN = "Unknown"
    }
}
