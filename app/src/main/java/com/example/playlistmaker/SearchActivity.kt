package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("search_query", searchQuery)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("search_query", "")
        val searchText = findViewById<EditText>(R.id.searchEditText)
        searchText.setText(searchQuery)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<Button>(R.id.btnSettingsBack)
        backButton.setOnClickListener {
            finish()
        }
        val RESPONSE_CODE = 200
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val noResultsLayout = findViewById<FrameLayout>(R.id.noResults)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val itunesSearchApi = retrofit.create(ItunesSearchApi::class.java)
        searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            fun makeNetworkRequest(query: String) {
                val call = itunesSearchApi.search(query)
                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>,
                    ) {
                        runOnUiThread {
                            val resultJson = response.body()
                            val searchResults: List<ItunesSearchResult> = Gson().fromJson(
                                resultJson?.getAsJsonArray("results"),
                                object : TypeToken<List<ItunesSearchResult>>() {}.type
                            )

                            if (searchResults.isEmpty()) {
                                noResultsLayout.visibility = View.VISIBLE
                            } else {
                                noResultsLayout.visibility = View.GONE
                            }
                            recyclerView.adapter = TrackAdapter(searchResults)
                        }
                    }

                    @SuppressLint("WrongView")
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        runOnUiThread {
                            if (t is HttpException) {
                                val response = t.response()
                                if (response != null && response.code() == RESPONSE_CODE) {
                                    val noResultsLayout = findViewById<FrameLayout>(R.id.noResults)
                                    noResultsLayout.visibility = View.VISIBLE
                                } else {
                                    val noInternetLayout =
                                        findViewById<FrameLayout>(R.id.noInternet)
                                    noInternetLayout.visibility = View.VISIBLE

                                    val refreshButton = findViewById<Button>(R.id.refresh)
                                    refreshButton.setOnClickListener {
                                        call.clone().enqueue(this)
                                        noInternetLayout.visibility = View.GONE
                                    }
                                }
                            } else {
                                val noInternetLayout = findViewById<FrameLayout>(R.id.noInternet)
                                noInternetLayout.visibility = View.VISIBLE

                                val refreshButton = findViewById<Button>(R.id.refresh)
                                refreshButton.setOnClickListener {
                                    call.clone().enqueue(this)
                                    noInternetLayout.visibility = View.GONE
                                }
                            }
                        }
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                makeNetworkRequest(query)
            }
        })


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
                // заглушка, добавить код для работы с поисковым запросом
            }
        })
        clearImageView.setOnClickListener {
            // Очищаем поисковый запрос
            searchEditText.setText("")
            // Скрываем клавиатуру
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

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearImageView.visibility = if (s.toString().isEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
}

