package com.squidink.xkcdviewer.views.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchItems
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.squareup.picasso.Picasso

class SearchAdapter(var data: GoogleSearchResponse, val picasso: Picasso, val listener: SearchItemClickListener): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail = view.findViewById<AppCompatImageView>(R.id.search_row_thumbnail)
        val title = view.findViewById<AppCompatTextView>(R.id.search_row_title)
        val snippet = view.findViewById<AppCompatTextView>(R.id.search_row_snippet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_recycler_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.items?.let{
            it[position].pageMap?.cseThumbnail?.let { thumb ->
                picasso.load(thumb[0].src).into(holder.thumbnail)
            }

            holder.title.text = it[position].title
            holder.snippet.text = it[position].snippet

            holder.itemView.setOnClickListener {_ -> listener.onItemClicked(it[position]) }
        }
        // Add OnClick to go back to activity and load relevant comic
    }

    override fun getItemCount(): Int {
        return data.items?.size ?: 0
    }

    interface SearchItemClickListener {
        fun onItemClicked(data: GoogleSearchItems)
    }
}