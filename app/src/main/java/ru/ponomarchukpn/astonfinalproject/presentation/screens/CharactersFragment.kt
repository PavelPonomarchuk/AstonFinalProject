package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentCharactersBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.CharactersAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharactersViewModel

class CharactersFragment : BaseFragment<FragmentCharactersBinding, CharactersViewModel>(
    CharactersViewModel::class.java
) {

    private val adapter by lazy {
        CharactersAdapter(
            onListEnded = {
                viewModel.onListEnded()
            },
            onItemClick = {
                launchDetailsFragment(it.id)
            }
        )
    }

    override fun createBinding(): FragmentCharactersBinding {
        return FragmentCharactersBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setOnRefreshListener()
        setButtonClearListener()
        setButtonFilterListener()
        setSearchViewListener()
        subscribeFlow()
        notifyViewModel()
    }

    private fun setAdapter() {
        binding.charactersRecycler.adapter = adapter
    }

    private fun setOnRefreshListener() {
        binding.charactersSwipeLayout.setOnRefreshListener {
            viewModel.onListSwiped()
        }
    }

    private fun setButtonClearListener() {
        //TODO
    }

    private fun setButtonFilterListener() {
        binding.charactersButtonFilter.setOnClickListener {
            launchFilterFragment()
        }
    }

    private fun setSearchViewListener() {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText)
                return true
            }
        }
        binding.charactersSearch.setOnQueryTextListener(listener)
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.charactersListState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { characters ->
                    adapter.submitList(characters)
                }
        }
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun launchDetailsFragment(characterId: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.host_container, CharacterDetailsFragment.newInstance(characterId))
            .addToBackStack(null)
            .commit()
    }

    private fun launchFilterFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.host_container, CharactersFilterFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {

        fun newInstance() = CharactersFragment()
    }
}
