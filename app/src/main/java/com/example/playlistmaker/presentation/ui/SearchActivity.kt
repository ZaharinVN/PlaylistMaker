package com.example.playlistmaker.presentation.ui


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
import com.example.playlistmaker.data.network.ItunesSearchApi
import com.example.playlistmaker.data.dto.ItunesSearchResult
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.TrackAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var searchQuery: String = ""
    private lateinit var noResultsLayout: FrameLayout
    private lateinit var noInternetLayout: FrameLayout
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: MutableList<ItunesSearchResult>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var clearHistoryButton: Button
    private lateinit var historyMessageTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressSearch: FrameLayout
    private lateinit var debounceHandler: Handler
    private val DEBOUNCE_DELAY_MILLIS = 2000L

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("search_query", searchQuery)
        super.onSaveInstanceState(outState)
    }

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
        progressBar = findViewById(R.id.progressBar) // Инициализация ProgressBar
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
        searchHistory = loadSearchHistory()
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        clearHistoryButton.visibility = if (searchHistory.isNotEmpty()) View.VISIBLE else View.GONE
        clearHistoryButton.setOnClickListener {
            clearSearchHistory()
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
        searchEditText.addTextChangedListener(object : TextWatcher {
            private var searchRunnable: Runnable = Runnable {
                searchQuery = searchEditText.text.toString()
                search(searchQuery, itunesSearchApi)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                debounceHandler.removeCallbacks(searchRunnable)
                debounceHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY_MILLIS)
            }
        })

        val trackAdapter = TrackAdapter(searchHistory) { track ->
            addTrackToHistory(track)
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

    // Метод для отображения/скрытия ProgressBar
    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        progressSearch.visibility = if (show) View.VISIBLE else View.GONE
    }
    private fun search(query: String, api: ItunesSearchApi) {
        showProgressBar(true) // Показать ProgressBar перед выполнением запроса

        val call = api.search(query)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                runOnUiThread {
                    val resultJson = response.body()
                    showProgressBar(false)// Скрыть ProgressBar после получения ответа
                    val searchResults: List<ItunesSearchResult> = Gson().fromJson(
                        resultJson?.getAsJsonArray("results"),
                        object : TypeToken<List<ItunesSearchResult>>() {}.type
                    )
                    if (searchResults.isEmpty()) {
                        noResultsLayout.visibility = View.VISIBLE
                        if (query.isEmpty()) {
                            noResultsLayout.visibility = View.GONE
                        }
                        val historyAdapter = TrackAdapter(searchHistory) { track ->
                            addTrackToHistory(track)
                        }
                        recyclerView.adapter = historyAdapter
                    } else {
                        val trackAdapter = TrackAdapter(searchResults) { track ->
                            addTrackToHistory(track)
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
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                fun showErrorLayout() {
                    noInternetLayout = findViewById(R.id.noInternet)
                    noInternetLayout.visibility = View.VISIBLE
                    refreshButton = findViewById(R.id.refresh)
                    refreshButton.setOnClickListener {
                        call.clone().enqueue(this)
                        noInternetLayout.visibility = View.GONE
                        showProgressBar(false)// Скрыть ProgressBar после получения ответа
                    }
                }
                runOnUiThread {
                    if (t is HttpException) {
                        val response = t.response()
                        if (response != null && response.code() == RESPONSE_CODE) {
                            noResultsLayout.visibility = View.VISIBLE
                            val historyAdapter = TrackAdapter(searchHistory) { track ->
                                addTrackToHistory(track)
                            }
                            recyclerView.adapter = historyAdapter
                        } else {
                            showErrorLayout()
                        }
                    } else {
                        showErrorLayout()
                    }
                }
            }
        })
    }

    private fun addTrackToHistory(track: ItunesSearchResult) {
        searchHistory.removeAll { it.trackId == track.trackId }
        searchHistory.add(0, track)
        if (searchHistory.size > 10) {
            searchHistory.removeLast()
        }
        saveSearchHistory()
    }

    private fun clearSearchHistory() {
        sharedPreferences.edit().remove(PREFERENCES_KEY).apply()
    }

    private fun saveSearchHistory() {
        val editor = sharedPreferences.edit()
        val searchHistoryJson = Gson().toJson(searchHistory)
        editor.putString(PREFERENCES_KEY, searchHistoryJson)
        editor.apply()
        startActivity(intent)
    }

    private fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        val searchHistoryJson = sharedPreferences.getString(PREFERENCES_KEY, null)
        val type = object : TypeToken<MutableList<ItunesSearchResult>>() {}.type
        return Gson().fromJson(searchHistoryJson, type) ?: mutableListOf()
    }
}
