package com.example.playlistmaker.search.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.example.playlistmaker.search.ui.model.ScreenState
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel.Companion.EXTRA_TRACK
import com.example.playlistmaker.utils.Debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var userInput = ""
    private lateinit var searchPlaceholder: FrameLayout
    private lateinit var noInternet: FrameLayout
    private lateinit var progressSearch: FrameLayout
    private lateinit var rvSearchResult: RecyclerView
    private lateinit var rvViewSavedTracks: RecyclerView
    private lateinit var searchHistory: RecyclerView
    private lateinit var noResultsMessage: TextView
    private lateinit var historyMessage: TextView
    private lateinit var noResultsImage: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var refreshButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var inputEditText: EditText
    private lateinit var progressBar: ProgressBar
    private val trackAdapter = TracksAdapter {
        onTrackClickDebounce(it)
    }
    private val savedTrackAdapter = TracksAdapter {
        onTrackClickDebounce(it)
    }
    private var textWatcher: TextWatcher? = null
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var onTrackClickDebounce: (TrackSearchModel) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPlaceholder = binding.noResults
        noResultsMessage = binding.textViewNoResults
        historyMessage = binding.historyMessage
        noInternet = binding.noInternet
        noResultsImage = binding.imageViewNoResults
        rvSearchResult = binding.rvSearchResult
        rvViewSavedTracks = binding.rvHistory
        refreshButton = binding.refresh
        inputEditText = binding.searchEditText
        clearButton = binding.clearImageView
        searchHistory = binding.rvSearchResult
        clearHistoryButton = binding.clearHistoryButton
        progressSearch = binding.progressSearch
        progressBar = binding.progressBar
        rvSearchResult.adapter = trackAdapter
        rvViewSavedTracks.adapter = savedTrackAdapter
        setListeners()
        searchHistory()
        onTrackClickDebounce = Debounce().debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            val mediaIntent = Intent(requireContext(), PlayerActivity::class.java).apply {
                putExtra(EXTRA_TRACK, it)
            }
            startActivity(mediaIntent)
            viewModel.addTrackToHistory(it)
        }
        viewModel.observeState().observe(viewLifecycleOwner, ::render)
        viewModel.getSavedTracksLiveData()
            .observe(viewLifecycleOwner) { savedTrackAdapter.updateTracks(it) }
    }

    private fun setListeners() {
        refreshButton.setOnClickListener {
            viewModel.search(userInput)
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            searchHistory.visibility = View.GONE
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateViewVisibility(s)
                userInput = s.toString()
                viewModel.searchDebounce(userInput)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher?.let { inputEditText.addTextChangedListener(it) }
    }

    private fun updateViewVisibility(s: CharSequence?) {
        binding.clearImageView.visibility = clearButtonVisibility(s)
        binding.rvSearchResult.visibility = View.GONE
        binding.rvHistory.visibility = View.VISIBLE
    }


    private fun searchHistory() {
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                viewModel.showHistoryTracks()
            }
        }
    }

    private fun render(state: ScreenState) {
        when (state) {
            is ScreenState.Loading -> Loading()
            is ScreenState.SearchedState -> SearchedState(state.tracks)
            is ScreenState.SavedState -> SavedState(state.tracks)
            is ScreenState.ErrorState -> ErrorState(state.errorMessage)
            is ScreenState.EmptyState -> EmptyState(state.message)
        }
    }

    private fun Loading() {
        progressSearch.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hideKeyboard()
    }

    private fun ErrorState(errorMessage: String) {
        progressBar.visibility = View.GONE
        noInternet.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE
        searchPlaceholder.visibility = View.VISIBLE
        noResultsImage.setImageResource(R.drawable.ic_no_internet)
        noResultsMessage.text = errorMessage
        hideKeyboard()
    }

    private fun EmptyState(message: String) {
        progressBar.visibility = View.GONE
        searchPlaceholder.visibility = View.VISIBLE
        noResultsImage.setImageResource(R.drawable.ic_no_results)
        noResultsMessage.text = message
    }

    private fun SearchedState(tracks: ArrayList<TrackSearchModel>) {
        searchPlaceholder.visibility = View.GONE
        searchHistory.visibility = View.GONE
        progressSearch.visibility = View.GONE
        progressBar.visibility = View.GONE
        noInternet.visibility = View.GONE
        refreshButton.visibility = View.GONE
        rvSearchResult.visibility = View.VISIBLE
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun SavedState(tracks: ArrayList<TrackSearchModel>) {
        searchPlaceholder.visibility = View.GONE
        progressBar.visibility = View.GONE
        refreshButton.visibility = View.GONE
        searchHistory.visibility = View.VISIBLE
        savedTrackAdapter.tracks.clear()
        savedTrackAdapter.tracks.addAll(tracks)
        savedTrackAdapter.notifyDataSetChanged()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun Fragment.hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus ?: View(requireContext())
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { inputEditText.removeTextChangedListener(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isEqual()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
    }
}



