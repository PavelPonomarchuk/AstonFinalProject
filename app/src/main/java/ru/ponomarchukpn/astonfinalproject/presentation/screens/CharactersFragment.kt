package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
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

    private var tabName: String? = null

    override fun createBinding(): FragmentCharactersBinding {
        return FragmentCharactersBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setOnRefreshListener()
        setButtonClearListener()
        setButtonFilterListener()
        setFilterChangedResultListener()
        setSearchViewListener()
        subscribeFlow()
        notifyViewModel()
    }

    private fun parseArguments() {
        val args = requireArguments()
        tabName = args.getString(KEY_TAB_NAME)
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

    private fun setFilterChangedResultListener() {
        setFragmentResultListener(KEY_FILTER_CHANGED) { key, bundle ->
            val isChanged = bundle.getBoolean(key)
            if (isChanged) {
                viewModel.onFilterSettingsChanged()
            }
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
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, CharacterDetailsFragment.newInstance(characterId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun launchFilterFragment() {
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, CharactersFilterFragment.newInstance())
                .addToBackStack(it)
                .commit()
        }
    }

    companion object {

        const val KEY_FILTER_CHANGED = "charactersFilterChanged"
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = CharactersFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
