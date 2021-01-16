package com.dowy.android.ui.movieDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dowy.android.data.Repository
import com.dowy.android.model.movie.MovieCast
import com.dowy.android.model.movie.MovieGenres
import com.dowy.android.model.movie.MovieReview
import com.dowy.android.model.movie.MovieTrailer
import com.dowy.android.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailsViewModel @ViewModelInject
constructor(private val repository: Repository) :
    ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listOfGenres = MutableLiveData<List<MovieGenres>>()
    val listOfGenres: LiveData<List<MovieGenres>>
        get() = _listOfGenres


    init {
        uiScope.launch {
            _listOfGenres.value = when (val movieGenres = repository.getMovieGenres()) {
                is Result.Success -> movieGenres.data
                is Result.Error -> null
            }
        }
    }

    fun getMovieReview(movieId: Int): LiveData<List<MovieReview>> {
        val reviewList = MutableLiveData<List<MovieReview>>()

        uiScope.launch {
            reviewList.value = when (val movieReviews = repository.getMovieReviews(movieId)) {
                is Result.Success -> {
                    if (movieReviews.data.size > 3) {
                        movieReviews.data.subList(0, 3)
                    } else {
                        movieReviews.data
                    }
                }
                is Result.Error -> null
            }
        }
        return reviewList
    }

    fun getMovieTrailers(movieId: Int): LiveData<List<MovieTrailer>> {
        val trailers = MutableLiveData<List<MovieTrailer>>()

        uiScope.launch {
            trailers.value = when (val result = repository.getMovieTrailer(movieId)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
        }
        return trailers
    }

    fun getMovieCast(movieId: Int): LiveData<List<MovieCast>> {
        val castMembers = MutableLiveData<List<MovieCast>>()

        uiScope.launch {
            castMembers.value = when (val result = repository.getMovieCast(movieId)) {
                is Result.Success -> result.data.filter { it.profile_path != null }
                is Result.Error -> null
            }
        }
        return castMembers
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}