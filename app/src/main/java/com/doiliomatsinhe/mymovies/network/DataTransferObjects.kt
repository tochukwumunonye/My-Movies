package com.doiliomatsinhe.mymovies.network

import com.doiliomatsinhe.mymovies.model.*
import com.google.gson.annotations.SerializedName

data class NetworkMovieReview(

    @SerializedName("id") val id: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieReview>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)

data class NetworkMovie(

    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val total_results: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<Movie>
)


data class NetworkMovieCredit(

    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<MovieCast>,
    @SerializedName("crew") val crew: List<MovieCrew>
)

data class NetworkMovieTrailer(

    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<MovieTrailer>
)

data class NetworkMovieGenres(

    @SerializedName("genres") val genres: List<MovieGenres>
)

data class NetworkTvSeries(

    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val total_results: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<TvSeries>
)