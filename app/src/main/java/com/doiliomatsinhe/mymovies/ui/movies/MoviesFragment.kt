package com.doiliomatsinhe.mymovies.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.doiliomatsinhe.mymovies.adapter.MovieAdapter
import com.doiliomatsinhe.mymovies.adapter.MovieClickListener
import com.doiliomatsinhe.mymovies.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        viewModel.listOfMovies.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

    }

    private fun initComponents() {
        adapter =
            MovieAdapter(MovieClickListener {
                Toast.makeText(activity, "${it.title} clicked!", Toast.LENGTH_SHORT).show()
            })

        binding.movieList.adapter = adapter
        binding.movieList.hasFixedSize()
        val layoutManager = GridLayoutManager(activity, 2)
        binding.movieList.layoutManager = layoutManager

    }
}