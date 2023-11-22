package com.example.playlistmaker.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.ui.history.FavoritesState
import com.example.playlistmaker.library.ui.viewModel.FavoriteViewModel
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.example.playlistmaker.search.ui.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoriteViewModel by viewModel()

    private val favoritesTracks = ArrayList<TrackSearchModel>()
    private val favoritesAdapter = TracksAdapter(favoritesTracks) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.ivNoFavorites.setImageResource(R.drawable.ic_no_results)
        binding.tvNoFavorites.text = getString(R.string.no_favorites)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesList.adapter = favoritesAdapter
    }

    private fun render(state: FavoritesState) {
        binding.apply {
            when (state) {
                is FavoritesState.Content -> {
                    favoritesList.visibility = View.GONE
                    favoritesEmpty.visibility = View.VISIBLE
                    favoritesTracks.clear()
                    favoritesTracks.addAll(favoritesTracks)
                    favoritesAdapter.notifyDataSetChanged()
                }

                else -> {
                    favoritesEmpty.visibility = View.VISIBLE
                    favoritesList.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}