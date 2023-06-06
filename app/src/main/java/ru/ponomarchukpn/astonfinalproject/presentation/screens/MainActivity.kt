package ru.ponomarchukpn.astonfinalproject.presentation.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.common.MyApp
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.CharactersAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.MainViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val appComponent by lazy {
        (application as MyApp).appComponent()
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.main_test_recycler)
        val adapter = CharactersAdapter(
            onListEnded = {
                viewModel.loadCharactersPage()
            },
            onCharacterClick = {
                viewModel.loadCharacter(it.id)
            }
        )
        recycler.adapter = adapter

        viewModel.loadCharactersPage()
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
