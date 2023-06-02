package ru.ponomarchukpn.astonfinalproject.presentation.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.CharactersAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.main_test_recycler)
        val adapter = CharactersAdapter()
        recycler.adapter = adapter

        adapter.onListEnded = {
            viewModel.loadCharactersPage()
        }
        adapter.onCharacterClick = {
            viewModel.loadCharacter(it.id)
            viewModel.loadLocation(1)
            viewModel.loadEpisode(1)
        }

        viewModel.loadCharactersPage()
        viewModel.loadLocationsPage()
        viewModel.loadEpisodesPage()
        viewModel.charactersLiveData.observe(this) {
            adapter.submitList(it)
        }
        viewModel.singleCharacterLiveData.observe(this) {
            it?.getContentIfNotHandled()?.let { entity ->
                showCharacterToast(entity)
            }
        }
    }

    private fun showCharacterToast(entity: CharacterEntity) {
        Toast.makeText(
            this,
            "Success: ${entity.id}, ${entity.name}",
            Toast.LENGTH_SHORT
        ).show()
    }
}
