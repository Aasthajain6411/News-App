package com.example.newsfresh

import android.media.Image
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewListAdapter( private val listener:  NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {
    //we are no longer getting data initially, we will get it after processing thats why we moved the items list inside the function.
    private val items : ArrayList<News> = ArrayList()
    //these 3 functions are implemented when we make an adapter of recyclerView

    //called when we create a viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        //layoutInflater converts xml to view that can be passed through class
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)

        val viewHolder= NewsViewHolder(view)
        //what will happen when we click the view (this should be responsibility of the activity not of the adapter)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    //binds data to holder
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text =currentItem.title
        holder.author.text=currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)
    }

    //only called first time and tells how many items are going to be there
    override fun getItemCount(): Int {
        return items.size
    }

    // giving items to adapter, cant give them directly so we make this function, it updates the news items
    fun updateNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)
        // will call all three above functions again
        //first getitemcount then create then onbind
        notifyDataSetChanged()
    }
}

//this itemView over here is the xml that we created item_news. These are the views that we have to inflate
class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView=itemView.findViewById(R.id.title)
    val image: ImageView=itemView.findViewById(R.id.image)
    val author: TextView=itemView.findViewById(R.id.author)

}

interface NewsItemClicked {
    fun onItemClicked(item : News)
    
}