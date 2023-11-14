package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.example.playlistmaker.search.ui.model.ScreenState
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val tracks = ArrayList<TrackSearchModel>()
    private val tracksHistory = ArrayList<TrackSearchModel>()
    private val searchAdapter = TracksAdapter(tracks) { trackClickListener(it) }
    private val historyAdapter = TracksAdapter(tracksHistory) { trackClickListener(it) }
    private var userInput = ""
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData().observe(viewLifecycleOwner) {
            updateScreen(it)
        }

        binding.apply {
            rvSearchResult.adapter = searchAdapter
            rvHistory.adapter = historyAdapter
            clearHistoryButton.visibility = View.GONE
            historyMessage.visibility = View.GONE
        }

        buttonsConfig()
        queryInputConfig(initTextWatcher())

    }

    private fun initTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.apply {
                clearImageView.visibility = clearButtonVisibility(s)
                rvSearchResult.visibility = View.GONE
            }
            userInput = s.toString()
            viewModel.searchDebounce(userInput, false)
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun buttonsConfig() {
        binding.apply {
            clearImageView.setOnClickListener {
                searchEditText.setText("")
                hideKeyboard()
                tracks.clear()
                viewModel.getTracksHistory()
                searchAdapter.notifyDataSetChanged()
                rvHistory.visibility = View.VISIBLE
                noInternet.visibility = View.GONE
                noResults.visibility = View.GONE
                clearHistoryButton.visibility =
                    if (tracksHistory.isEmpty()) View.GONE else View.VISIBLE
                historyMessage.visibility = if (tracksHistory.isEmpty()) View.GONE else View.VISIBLE
            }
            clearHistoryButton.setOnClickListener {
                viewModel.clearHistory()
                viewModel.getTracksHistory()
                rvHistory.visibility = View.GONE
                historyMessage.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
            }

            refresh.setOnClickListener {
                viewModel.debouncedSearch(userInput)
            }
        }
    }

    private fun queryInputConfig(textWatcher: TextWatcher) {
        binding.searchEditText.apply {
            addTextChangedListener(textWatcher)
            setText(userInput)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && this.text.isEmpty())
                    viewModel.getTracksHistory()
                else
                    binding.rvHistory.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, userInput)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun trackClickListener(track: TrackSearchModel) {
        viewModel.onTrackClick(track)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateScreen(state: ScreenState) {
        binding.apply {
            when (state) {
                is ScreenState.Content -> {
                    tracks.clear()
                    tracks.addAll(state.tracks as ArrayList<TrackSearchModel>)
                    progressSearch.visibility = View.GONE
                    rvSearchResult.visibility = View.VISIBLE
                    noInternet.visibility = View.GONE
                    noResults.visibility = View.GONE
                    searchAdapter.notifyDataSetChanged()
                }

                is ScreenState.Error -> {
                    progressSearch.visibility = View.GONE
                    noInternet.visibility = View.VISIBLE
                    noResults.visibility = View.GONE
                    imageViewNoInternet.setImageResource(R.drawable.ic_no_internet)
                    textViewNoInternet.setText(R.string.noInternet)
                    refresh.visibility = View.VISIBLE
                    clearHistoryButton.visibility = View.GONE
                    historyMessage.visibility = View.GONE
                }

                is ScreenState.Empty -> {
                    progressSearch.visibility = View.GONE
                    noInternet.visibility = View.GONE
                    noResults.visibility = View.VISIBLE
                    clearHistoryButton.visibility = View.GONE
                    historyMessage.visibility = View.GONE
                    imageViewNoResults.setImageResource(R.drawable.ic_no_results)
                    textViewNoResults.setText(R.string.NoResults)
                    refresh.visibility = View.GONE
                }

                is ScreenState.Loading -> {
                    noInternet.visibility = View.GONE
                    noResults.visibility = View.GONE
                    rvHistory.visibility = View.GONE
                    rvSearchResult.visibility = View.GONE
                    progressSearch.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE
                    hideKeyboard()
                }

                is ScreenState.ContentHistoryList -> {
                    rvHistory.visibility = View.VISIBLE
                    tracksHistory.clear()
                    tracksHistory.addAll(state.historyList)
                    historyAdapter.notifyDataSetChanged()
                }

                is ScreenState.EmptyHistoryList -> {
                    rvHistory.visibility = View.GONE
                    clearHistoryButton.visibility = View.GONE
                    historyMessage.visibility = View.GONE
                }
            }
        }
    }

    fun Fragment.hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus ?: View(requireContext())
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val USER_INPUT = "USER_INPUT"
    }
}