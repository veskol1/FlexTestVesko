package com.example.flextestvesko.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flextestvesko.connectivity.ConnectivityObserver
import com.example.flextestvesko.connectivity.NetworkConnectivityManager
import com.example.flextestvesko.models.Movie
import com.example.flextestvesko.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val connectivityManager: NetworkConnectivityManager ,private val repository: MovieRepository) : ViewModel() {
    init {
        Log.d("haha", " on init")
        connectivityManager.observe().onEach { status ->
            when (status) {
                ConnectivityObserver.Status.Available -> {
                    updateState(uiState.value.listType)
                }
                ConnectivityObserver.Status.Lost -> {
                    updateState(uiState.value.listType)
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
    private fun getMoviesData(listType: ListType) = Pager(
        PagingConfig(pageSize = 20)
    ) {
        if (listType == ListType.FAVORITES) {
            repository.getFavoriteMovieList()
        } else {
            repository.getRemoteMovieList(listType = listType)
        }
    }.flow
        .cachedIn(viewModelScope)

    private val _uiState =
        MutableStateFlow(
            UiState(
                listType = ListType.POPULAR,
                movies = getMoviesData(ListType.POPULAR),
            )
        )
    var uiState: StateFlow<UiState> = _uiState.asStateFlow()


    private val _detailMovieUiState =
        MutableStateFlow(
            DetailMovieUiState(
                movie = null,
                isFavorite = false
            )
        )
    var detailMovieUiState: StateFlow<DetailMovieUiState> = _detailMovieUiState.asStateFlow()

    fun updateState(selectedType: ListType) {
        when (selectedType) {
            ListType.POPULAR -> {
                _uiState.update { state ->
                    state.copy(
                        listType = ListType.POPULAR,
                        movies = getMoviesData(ListType.POPULAR)
                    )
                }
            }
            ListType.NEW_MOVIES -> {
                _uiState.update { state ->
                    state.copy(
                        listType = ListType.NEW_MOVIES,
                        movies = getMoviesData(ListType.NEW_MOVIES)
                    )
                }
            }
            ListType.FAVORITES -> {
                _uiState.update { state ->
                    state.copy(
                        listType = ListType.FAVORITES,
                        movies = getMoviesData(ListType.FAVORITES)
                    )
                }
            }
        }
    }

    fun updateClickedMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailMovieUiState.update {
                it.copy(
                    movie = movie,
                    isFavorite = repository.checkIfMovieIsFavorite(movie = movie)
                )
            }
        }
    }

    fun updateFavoriteMovieState(isChecked: Boolean) {
        _detailMovieUiState.update {
            it.copy(
                isFavorite = isChecked,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (isChecked) {
                repository.insertMovieToDb(detailMovieUiState.value.movie!!)
            } else {
                repository.deleteMovieToDb(detailMovieUiState.value.movie!!)
            }
        }
    }
}

data class UiState(
    val listType: ListType,
    val movies: Flow<PagingData<Movie>>?,
)

data class DetailMovieUiState(
    val movie: Movie?,
    val isFavorite: Boolean,
)

enum class ListType {
    POPULAR,
    NEW_MOVIES,
    FAVORITES
}
