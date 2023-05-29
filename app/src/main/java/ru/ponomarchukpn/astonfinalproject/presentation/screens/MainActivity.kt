package ru.ponomarchukpn.astonfinalproject.presentation.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.ponomarchukpn.astonfinalproject.R
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
            viewModel.loadNextPage()
        }

        viewModel.loadNextPage()
        viewModel.charactersLiveData.observe(this) {
            adapter.submitList(it)
        }
    }
}
