package com.example.flextestvesko.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.flextestvesko.R
import com.example.flextestvesko.databinding.FragmentMovieDetailsBinding
import com.example.flextestvesko.utils.Constants.POSTER_LAND_BASE_URL
import com.example.flextestvesko.viewmodels.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MovieViewModel by activityViewModels()
    private var updateUiDone = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviesViewModel.detailMovieUiState.collectLatest { state ->

                    Glide.with(this@MovieDetailsFragment)
                        .load(POSTER_LAND_BASE_URL + state.movie?.imgLandUrl)
                        .into(binding.movieLandscapePoster)

                    binding.movieTitle.text = state.movie?.title
                    binding.movieOverview.text = state.movie?.overview
                    binding.icon.isChecked = state.isFavorite
                    updateUiDone = true
                }
            }
        }

        binding.icon.setOnCheckedChangeListener { _, isChecked ->
            moviesViewModel.updateFavoriteMovieState(isChecked)

            if (updateUiDone) {
                Toast.makeText(requireContext(),
                    resources.getString(R.string.movie_saved).takeIf { isChecked }
                        ?: resources.getString(R.string.movie_removed),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}