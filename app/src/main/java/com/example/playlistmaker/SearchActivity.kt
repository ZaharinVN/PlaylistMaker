package com.example.playlistmaker


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("search_query", searchQuery)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val RESPONSE_CODE = 200
        const val PREFERENCES_KEY = "search_history"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<Button>(R.id.btnSettingsBack)
        backButton.setOnClickListener { finish() }
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        noResultsLayout = findViewById(R.id.noResults)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
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
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val itunesSearchApi = retrofit.create(ItunesSearchApi::class.java)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
                search(searchQuery, itunesSearchApi)
            }
        })
        val trackAdapter = TrackAdapter(searchHistory) { track ->
            addTrackToHistory(track)
        }
        recyclerView.adapter = trackAdapter
        val clearImageView = findViewById<ImageView>(R.id.clearImageView)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length ?: 0 > 0) {
                    clearImageView.visibility = View.VISIBLE
                } else {
                    clearImageView.visibility = View.GONE
                }
                searchQuery = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 0) {
                    clearImageView.visibility = View.VISIBLE
                } else {
                    clearImageView.visibility = View.GONE
                }
                searchQuery = s.toString()

            }
        })
        clearImageView.setOnClickListener {
            // Очищаем поисковый запрос
            searchEditText.setText("")
            clearImageView.visibility = View.GONE// Скрываем клавиатуру
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            // Скрываем кнопку сброса
            clearImageView.visibility = View.GONE

        }
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            // Отображение клавиатуры и фокуса на поле ввода поискового запроса
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
                clearImageView.visibility =
                    if (searchEditText.text.toString().isEmpty()) View.GONE else View.VISIBLE
            }
            // Скрытие кнопки сброса поискового запроса и клавиатуры при потере фокуса
            else {
                clearImageView.visibility = View.GONE
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            }
        }

    }

    private fun search(query: String, api: ItunesSearchApi) {
        val call = api.search(query)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                runOnUiThread {
                    val resultJson = response.body()
                    val searchResults: List<ItunesSearchResult> = Gson().fromJson(
                        resultJson?.getAsJsonArray("results"),
                        object : TypeToken<List<ItunesSearchResult>>() {}.type
                    )
                    if (searchResults.isEmpty()) {
                        val historyAdapter = TrackAdapter(searchHistory) { track ->
                            addTrackToHistory(track)
                        }
                        recyclerView.adapter = historyAdapter
                    } else {
                        val trackAdapter = TrackAdapter(searchResults) { track ->
                            addTrackToHistory(track)
                        }
                        recyclerView.adapter = trackAdapter
                    }
                }
            }

            fun showErrorLayout() {
                noInternetLayout = findViewById(R.id.noInternet)
                noInternetLayout.visibility = View.VISIBLE
                refreshButton = findViewById(R.id.refresh)
                refreshButton.setOnClickListener {
                    call.clone().enqueue(this)
                    noInternetLayout.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                runOnUiThread {
                    if (t is HttpException) {
                        val response = t.response()
                        if (response != null && response.code() == RESPONSE_CODE) {
                            val noResultsLayout = findViewById<FrameLayout>(R.id.noResults)
                            noResultsLayout.visibility = View.VISIBLE
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
    }


    private fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        val searchHistoryJson = sharedPreferences.getString(PREFERENCES_KEY, null)
        val type = object : TypeToken<MutableList<ItunesSearchResult>>() {}.type
        return Gson().fromJson(searchHistoryJson, type) ?: mutableListOf()
    }
}






