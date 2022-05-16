package com.capstone.pet2home.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pet2home.R
import com.capstone.pet2home.model.Post
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent

class ListPostUserAdapter(
    private val listPost: ArrayList<Post>,
    private var optionsMenuClickListener: OptionsMenuClickListener
    ) : RecyclerView.Adapter<ListPostUserAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView = itemView.findViewById(R.id.img_item_photo)
        var postTitle: TextView = itemView.findViewById(R.id.tv_item_name)
        var postLocation: TextView = itemView.findViewById(R.id.tv_item_description)
        var btnMenu: View = itemView.findViewById(R.id.btn_menu_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_post, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, location, image) = listPost[position]
        holder.postImage.setImageResource(image)
        holder.postTitle.text = title
        holder.postLocation.text = location

        holder.btnMenu.setOnClickListener{
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }
    }

    override fun getItemCount(): Int = listPost.size

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }
}