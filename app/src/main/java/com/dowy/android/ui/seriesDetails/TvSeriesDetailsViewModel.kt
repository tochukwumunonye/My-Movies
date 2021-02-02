package com.dowy.android.ui.seriesDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dowy.android.data.Repository
import com.dowy.android.model.tv.TvCast
import com.dowy.android.model.tv.TvGenres
import com.dowy.android.model.tv.TvReview
import com.dowy.android.model.tv.TvTrailer
import com.dowy.android.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TvSeriesDetailsViewModel @ViewModelInject
constructor(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // In-Memory Caching
    private var currentTvCastResult: MutableLiveData<List<TvCast>>? = null
    private var currentTvTrailersResult: MutableLiveData<List<TvTrailer>>? = null
    private var currentTvReviewResult: MutableLiveData<List<TvReview>>? = null
    private var currentMovieGenreResult: MutableLiveData<List<TvGenres>>? = null
    private var currentTvId: Int? = null
    private var currentLanguage: String? = null

    fun getTvGenre(language: String): LiveData<List<TvGenres>> {
        val genreList = MutableLiveData<List<TvGenres>>()

        val lastResult = currentMovieGenreResult
        if (language == currentLanguage && lastResult != null) {
            return lastResult
        }

        currentLanguage = language
        uiScope.launch {
            genreList.value = when (val tvGenres = repository.getTvGenres(language)) {
                is Result.Success -> tvGenres.data
                is Result.Error -> null
            }
            currentMovieGenreResult = genreList
        }
        return genreList
    }

    fun getTvReview(tvId: Int): LiveData<List<TvReview>> {
        val reviewList = MutableLiveData<List<TvReview>>()

        val lastResult = currentTvReviewResult
        if (tvId == currentTvId && lastResult != null) {
            return lastResult
        }

        currentTvId = tvId
        uiScope.launch {
            reviewList.value = when (val tvReviews = repository.getTvReviews(tvId)) {
                is Result.Success -> {
                    if (tvReviews.data.size > 3) {
                        tvReviews.data.subList(0, 3)
                    } else {
                        tvReviews.data
                    }
                }
                is Result.Error -> null
            }
            currentTvReviewResult = reviewList
        }
        return reviewList
    }

    fun getTvTrailers(tvId: Int, language: String): LiveData<List<TvTrailer>> {
        val trailers = MutableLiveData<List<TvTrailer>>()

        val lastResult = currentTvTrailersResult
        if (tvId == currentTvId && language == currentLanguage && lastResult != null) {
            return lastResult
        }

        currentTvId = tvId
        uiScope.launch {
            trailers.value = when (val result = repository.getTvTrailer(tvId, language)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            currentTvTrailersResult = trailers
        }
        return trailers
    }

    fun getTvCast(tvId: Int): LiveData<List<TvCast>> {
        val castMembers = MutableLiveData<List<TvCast>>()

        val lastResult = currentTvCastResult
        if (tvId == currentTvId && lastResult != null) {
            return lastResult
        }

        currentTvId = tvId
        uiScope.launch {
            castMembers.value = when (val result = repository.getTvCast(tvId)) {
                is Result.Success -> result.data.filter { it.profile_path != null }
                is Result.Error -> null
            }
            currentTvCastResult = castMembers
        }
        return castMembers
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}