package com.dowy.android.adapter.trailer

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dowy.android.model.movie.MovieTrailer
import com.dowy.android.model.tv.TvTrailer
import com.dowy.android.utils.MOVIE_VIEW_TYPE
import com.dowy.android.utils.SERIES_VIEW_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

class TrailerAdapter(private val clickListener: TrailerClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(TrailerDiffUtilCallbak()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_VIEW_TYPE -> MovieTrailerViewHolder.from(parent)
            SERIES_VIEW_TYPE -> SeriesTrailerViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieTrailerViewHolder -> {
                val movieTrailer = getItem(position) as DataItem.MovieTrailerItem
                holder.bind(movieTrailer.movieTrailer, clickListener)
            }
            is SeriesTrailerViewHolder -> {
                val seriesTrailer = getItem(position) as DataItem.SeriesTrailerItem
                holder.bind(
                    seriesTrailer.seriesTrailer,
                    clickListener
                )
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.MovieTrailerItem -> MOVIE_VIEW_TYPE
            is DataItem.SeriesTrailerItem -> SERIES_VIEW_TYPE
        }
    }

    fun submitMovieTrailers(list: List<MovieTrailer>) {
        adapterScope.launch {
            val listOfTrailers = list.map {
                DataItem.MovieTrailerItem(it)
            }

            withContext(Dispatchers.Main) {
                submitList(listOfTrailers)
            }
        }
    }

    fun submitSeriesTrailers(list: List<TvTrailer>) {
        adapterScope.launch {
            val listOfTrailers = list.map {
                DataItem.SeriesTrailerItem(it)
            }

            withContext(Dispatchers.Main) {
                submitList(listOfTrailers)
            }
        }
    }

}

class TrailerDiffUtilCallbak : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class TrailerClickListener(val clickListener: (trailer: Any) -> Unit) {
    fun onCLick(trailer: Any) {
        when (trailer) {
            is MovieTrailer -> clickListener(trailer)
            is TvTrailer -> clickListener(trailer)
        }
    }
}


sealed class DataItem {
    data class MovieTrailerItem(val movieTrailer: MovieTrailer) : DataItem() {
        override val id: String
            get() = movieTrailer.id
    }

    data class SeriesTrailerItem(val seriesTrailer: TvTrailer) : DataItem() {
        override val id: String
            get() = seriesTrailer.id
    }

    abstract val id: String
}