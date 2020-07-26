package com.doiliomatsinhe.mymovies.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.doiliomatsinhe.mymovies.data.db.DatabaseMovie
import com.google.gson.annotations.SerializedName

data class Movie(

    @SerializedName("popularity") val popularity: Double,
    @SerializedName("vote_count") val vote_count: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("id") val id: Int,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val vote_average: Double,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String
) {
    val fullPosterPath: String
        get() = "http://image.tmdb.org/t/p/w342$poster_path"
}

fun List<DatabaseMovie>.asDomainModel(): List<Movie> {

    return map {
        Movie(
            popularity = it.popularity,
            vote_count = it.vote_count,
            video = it.video,
            poster_path = it.poster_path,
            id = it.id,
            adult = it.adult,
            backdrop_path = it.backdrop_path,
            original_language = it.original_language,
            original_title = it.original_title,
            genre_ids = it.genre_ids,
            title = it.title,
            vote_average = it.vote_average,
            overview = it.overview,
            release_date = it.release_date
        )
    }
}

fun LiveData<DatabaseMovie>.asDomainModel(): LiveData<Movie> {
    return map {
        Movie(
            popularity = it.popularity,
            vote_count = it.vote_count,
            video = it.video,
            poster_path = it.poster_path,
            id = it.id,
            adult = it.adult,
            backdrop_path = it.backdrop_path,
            original_language = it.original_language,
            original_title = it.original_title,
            genre_ids = it.genre_ids,
            title = it.title,
            vote_average = it.vote_average,
            overview = it.overview,
            release_date = it.release_date
        )
    }
}