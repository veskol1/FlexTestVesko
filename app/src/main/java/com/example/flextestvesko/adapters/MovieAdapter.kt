package com.example.flextestvesko.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.flextestvesko.databinding.MovieItemBinding
import com.example.flextestvesko.models.Movie
import com.example.flextestvesko.utils.Constants.POSTER_BASE_URL


class MovieAdapter(private val listener: MovieClickListener) : PagingDataAdapter<Movie, MovieAdapter.ImageViewHolder>(diffCallback) {


    inner class ImageViewHolder(val binding: MovieItemBinding) : ViewHolder(binding.root)

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val movie = getItem(position)

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(POSTER_BASE_URL + movie?.imgUrl)
                .into(imageView)

            root.setOnClickListener {
                movie?.let {
                    listener.onMovieClicked(movie = it)
                }
            }
        }
    }

    interface MovieClickListener {
        fun onMovieClicked(movie: Movie)
    }

}

