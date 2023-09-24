package com.example.playlistmaker.search


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.main.ui.MainActivity
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.data.ItunesSearchResult
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.domain.HistoryUseCase
import com.example.playlistmaker.search.domain.SearchUseCase
import com.example.playlistmaker.search.ui.TrackAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.playlistmaker.search.data.ItunesSearchApi as ItunesSearchApi


class SearchActivity : AppCompatActivity() {
    private var searchQuery: String = ""
    private lateinit var noResultsLayout: FrameLayout
    private lateinit var noInternetLayout: FrameLayout
    private lateinit var refreshButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var historyMessageTextView: TextView
    private lateinit var searchHistory: MutableList<ItunesSearchResult>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var progressSearch: FrameLayout
    private lateinit var debounceHandler: Handler
    private val DEBOUNCE_DELAY_MILLIS = 2000L
    private lateinit var historyUseCase: HistoryUseCase
    private lateinit var historyRepository: HistoryRepository
    private lateinit var searchUseCase: SearchUseCase
    private lateinit var searchRepository: SearchRepository

    companion object {
        const val RESPONSE_CODE = 200
        const val PREFERENCES_KEY = "search_history"
        const val EXTRA_TRACK_ID = "trackId"
        const val EXTRA_TRACK_NAME = "trackName"
        const val EXTRA_ARTIST_NAME = "artistName"
        const val EXTRA_TRACK_TIME = "trackTimeMillis"
        const val EXTRA_TRACK_COVER = "trackCover"
        const val EXTRA_COLLECTION_NAME = "collectionName"
        const val EXTRA_RELEASE_DATE = "releaseDate"
        const val EXTRA_PRIMARY_GENRE_NAME = "primaryGenreName"
        const val EXTRA_COUNTRY = "country"
        const val EXTRA_PREVIEW = "previewUrl"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("search_query", searchQuery)
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("search_query", "")
        val searchText = findViewById<EditText>(R.id.searchEditText)
        searchText.setText(searchQuery)
        if (searchQuery.isEmpty()) {
            searchText.requestFocus()
        }
    }
    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        progressBar = findViewById(R.id.progressBar)
        progressSearch = findViewById(R.id.progressSearch)
        val backButton = findViewById<Button>(R.id.btnSettingsBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val clearImageView = findViewById<ImageView>(R.id.clearImageView)
        val searchText = findViewById<EditText>(R.id.searchEditText)
        clearImageView.setOnClickListener {
            searchText.text.clear()
            clearImageView.visibility = View.GONE
        }
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        noResultsLayout = findViewById(R.id.noResults)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        sharedPreferences = getSharedPreferences("Search History", Context.MODE_PRIVATE)
        historyRepository = Creator.createHistoryRepository(sharedPreferences)
        historyUseCase = Creator.createHistoryUseCase(historyRepository)
        searchHistory = historyUseCase.loadSearchHistory()
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        clearHistoryButton.visibility = if (searchHistory.isNotEmpty()) View.VISIBLE else View.GONE
        clearHistoryButton.setOnClickListener {
            historyUseCase.clearSearchHistory()
            searchHistory.clear()
            recyclerView.adapter?.notifyDataSetChanged()
            clearHistoryButton.visibility = View.GONE
            historyMessageTextView.visibility = View.GONE
        }
        historyMessageTextView = findViewById(R.id.history_message)
        historyMessageTextView.visibility =
            if (searchHistory.isNotEmpty()) View.VISIBLE else View.GONE
        debounceHandler = Handler(Looper.getMainLooper())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val itunesSearchApi = retrofit.create(ItunesSearchApi::class.java)
        searchRepository = Creator.createSearchRepository(itunesSearchApi)
        searchUseCase = Creator.createSearchUseCase(searchRepository)
        searchEditText.addTextChangedListener(object : TextWatcher {
            private var searchRunnable: Runnable = Runnable {
                searchQuery = searchEditText.text.toString()
                search(searchQuery, searchUseCase)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                debounceHandler.removeCallbacks(searchRunnable)
                debounceHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY_MILLIS)
            }
        })
        val trackAdapter = TrackAdapter(searchHistory) { track ->
            addTrackToHistory(track as ItunesSearchResult)
            val intent = Intent(this@SearchActivity, MediaActivity::class.java).apply {
                putExtra(EXTRA_TRACK_ID, track.trackId)
                putExtra(EXTRA_TRACK_NAME, track.trackName)
                putExtra(EXTRA_ARTIST_NAME, track.artistName)
                putExtra(EXTRA_TRACK_TIME, track.trackTimeMillis)
                putExtra(EXTRA_TRACK_COVER, track.artworkUrl100)
                putExtra(EXTRA_COLLECTION_NAME, track.collectionName)
                putExtra(EXTRA_RELEASE_DATE, track.releaseDate)
                putExtra(EXTRA_PRIMARY_GENRE_NAME, track.primaryGenreName)
                putExtra(EXTRA_COUNTRY, track.country)
                putExtra(EXTRA_PREVIEW, track.previewUrl)
            }
            startActivity(intent)
        }
        recyclerView.adapter = trackAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) > 0) {
                    clearImageView.visibility = View.VISIBLE
                } else {
                    clearImageView.visibility = View.GONE
                }
                searchQuery = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > 0) {
                    clearImageView.visibility = View.VISIBLE
                } else {
                    clearImageView.visibility = View.GONE
                }
                searchQuery = s.toString()
            }
        })
        clearImageView.setOnClickListener { // Очищаем поисковый запрос
            searchEditText.text.clear()
            clearImageView.visibility = View.GONE
            // Скрываем клавиатуру
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            // Скрываем кнопку сброса
            clearImageView.visibility = View.GONE
        }
        searchEditText.setOnFocusChangeListener { v, hasFocus -> // Отображение клавиатуры и фокуса на поле ввода поискового запроса
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
                clearImageView.visibility =
                    if (searchEditText.text.toString().isEmpty()) View.GONE else View.VISIBLE
            } else { // Скрытие кнопки сброса поискового запроса и клавиатуры при потере фокуса
                clearImageView.visibility = View.GONE
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            }
        }
    }
    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        progressSearch.visibility = if (show) View.VISIBLE else View.GONE
    }
    private fun search(query: String, searchUseCase: SearchUseCase) {
        showProgressBar(true)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        searchUseCase.search(query,
            onResponse = { searchResults ->
                runOnUiThread {
                    showProgressBar(false)
                    if (searchResults.isEmpty()) {
                        noResultsLayout.visibility = View.VISIBLE
                        if (query.isEmpty()) {
                            noResultsLayout.visibility = View.GONE
                        }
                        val historyAdapter = TrackAdapter(searchHistory) { track ->
                            addTrackToHistory(track as ItunesSearchResult)
                        }
                        recyclerView.adapter = historyAdapter
                    } else {
                        val trackAdapter = TrackAdapter(searchResults) { track ->
                            addTrackToHistory(track as ItunesSearchResult)
                            val intent =
                                Intent(this@SearchActivity, MediaActivity::class.java).apply {
                                    putExtra(EXTRA_TRACK_ID, track.trackId)
                                    putExtra(EXTRA_TRACK_NAME, track.trackName)
                                    putExtra(EXTRA_ARTIST_NAME, track.artistName)
                                    putExtra(EXTRA_TRACK_TIME, track.trackTimeMillis)
                                    putExtra(EXTRA_TRACK_COVER, track.artworkUrl100)
                                    putExtra(EXTRA_COLLECTION_NAME, track.collectionName)
                                    putExtra(EXTRA_RELEASE_DATE, track.releaseDate)
                                    putExtra(EXTRA_PRIMARY_GENRE_NAME, track.primaryGenreName)
                                    putExtra(EXTRA_COUNTRY, track.country)
                                    putExtra(EXTRA_PREVIEW, track.previewUrl)
                                }
                            startActivity(intent)
                        }
                        recyclerView.adapter = trackAdapter
                    }
                }
            },
            onFailure = { t ->
                fun showErrorLayout() {
                    noInternetLayout = findViewById(R.id.noInternet)
                    noInternetLayout.visibility = View.VISIBLE
                    refreshButton = findViewById(R.id.refresh)
                    refreshButton.setOnClickListener {
                        search(query, searchUseCase)
                        noInternetLayout.visibility = View.GONE
                        showProgressBar(false)
                    }
                }
                runOnUiThread {
                    if (t is HttpException) {
                        val response = t.response()
                        if (response != null && response.code() == RESPONSE_CODE) {
                            noResultsLayout.visibility = View.VISIBLE
                            val historyAdapter = TrackAdapter(searchHistory) { track ->
                                addTrackToHistory(track as ItunesSearchResult)
                            }
                            recyclerView.adapter = historyAdapter
                        } else {
                            showErrorLayout()
                    }
                }
            }
        })
    }

    private fun addTrackToHistory(track: ItunesSearchResult) {
        historyUseCase.addTrackToHistory(track)
        startActivity(intent)
    }
    private fun clearSearchHistory() {
        historyUseCase.clearSearchHistory()
    }

    private fun saveSearchHistory() {
        historyUseCase.saveSearchHistory()
    }
    private fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        return historyUseCase.loadSearchHistory()
    }
}








