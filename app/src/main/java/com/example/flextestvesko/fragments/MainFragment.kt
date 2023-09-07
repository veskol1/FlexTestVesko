package com.example.flextestvesko.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flextestvesko.R
import com.example.flextestvesko.adapters.MovieAdapter
import com.example.flextestvesko.databinding.FragmentMainBinding
import com.example.flextestvesko.models.Movie
import com.example.flextestvesko.utils.Constants.FAVORITE_MOVIE
import com.example.flextestvesko.utils.Constants.NEW_MOVIE
import com.example.flextestvesko.utils.Constants.POPULAR_MOVIE
import com.example.flextestvesko.viewmodels.ListType
import com.example.flextestvesko.viewmodels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), MovieAdapter.MovieClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private val moviesViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val movieListType = resources.getStringArray(R.array.movie_lists)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, movieListType)
        binding.dropDown.setAdapter(arrayAdapter)

        binding.dropDown.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                POPULAR_MOVIE -> moviesViewModel.updateState(selectedType = ListType.POPULAR)
                NEW_MOVIE ->  moviesViewModel.updateState(selectedType = ListType.NEW_MOVIES)
                FAVORITE_MOVIE -> moviesViewModel.updateState(selectedType = ListType.FAVORITES)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviesViewModel.uiState.collectLatest { state ->
                    state.movies?.collectLatest {
                        movieAdapter.submitData(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieAdapter.loadStateFlow.collectLatest { loadStates ->
                    binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    binding.errorImage.isVisible = loadStates.refresh is LoadState.Error
                }
            }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(this@MainFragment)
        binding.recyclerView.apply {
            adapter = movieAdapter
            layoutManager = StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClicked(movie: Movie) {
        moviesViewModel.updateClickedMovie(movie)
        findNavController().navigate(directions = MainFragmentDirections.actionMainFragmentToMovieDetailsFragment())
    }

}