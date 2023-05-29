package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.common.showToast
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentCharactersBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.CharactersAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharactersViewModel

class CharactersFragment : BaseFragment<FragmentCharactersBinding, CharactersViewModel>(
    CharactersViewModel::class.java
) {

    private val adapter by lazy {
        CharactersAdapter(
            onListEnded = {
                viewModel.onListEnded()
                startProgress()
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
        configureSwipeLayout()
        setOnRefreshListener()
        setButtonFilterListener()
        setFilterChangedResultListener()
        setSearchViewListener()
        subscribeFlow()
        notifyViewModel()
        startProgress()
    }

    private fun parseArguments() {
        val args = requireArguments()
        tabName = args.getString(KEY_TAB_NAME)
    }

    private fun setAdapter() {
        binding.charactersRecycler.adapter = adapter
    }

    private fun configureSwipeLayout() {
        binding.charactersSwipeLayout.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener() {
        binding.charactersSwipeLayout.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.charactersListState
                    .onEach {
                        processCharactersList(it)
                    }
                    .launchIn(this)

                viewModel.notEmptyFilterState
                    .onEach {
                        setButtonClearState(it)
                    }
                    .launchIn(this)

                viewModel.errorState
                    .onEach {
                        showError()
                        stopProgress()
                    }
                    .launchIn(this)

                viewModel.emptyResultState
                    .onEach { showEmptyResultToast() }
                    .launchIn(this)
            }
        }
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun processCharactersList(characters: List<CharacterEntity>) {
        adapter.submitList(characters)
        stopProgress()
    }

    private fun startProgress() {
        binding.charactersProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.charactersProgress.visibility = View.INVISIBLE
    }

    private fun showError() {
        val errorMessage = getString(R.string.error_downloading_list)
        requireContext().showToast(errorMessage)
    }

    private fun showEmptyResultToast() {
        val emptyResultMessage = getString(R.string.message_empty_results)
        requireContext().showToast(emptyResultMessage)
    }

    private fun setButtonClearState(notEmptyFilter: Boolean) {
        if (notEmptyFilter) {
            binding.charactersButtonClear.setBackgroundResource(R.drawable.colored_button_ripple)
            binding.charactersButtonClear.setOnClickListener {
                viewModel.onButtonClearPressed()
                startProgress()
            }
        } else {
            binding.charactersButtonClear.setBackgroundResource(R.drawable.gray_button_shape)
            binding.charactersButtonClear.setOnClickListener(null)
        }
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
