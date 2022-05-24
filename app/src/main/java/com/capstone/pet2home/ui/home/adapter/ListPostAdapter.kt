package com.capstone.pet2home.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pet2home.R
import com.capstone.pet2home.model.Post

class ListPostAdapter(
    private val listPost: ArrayList<Post>,
    private var optionsMenuClickListener: OptionsMenuClickListener,
    private val binding: RecyclerView,
) : RecyclerView.Adapter<ListPostAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView = itemView.findViewById(R.id.img_item_photo)
        var postTitle: TextView = itemView.findViewById(R.id.tv_item_name)
        var postLocation: TextView = itemView.findViewById(R.id.tv_item_description)
        var btnMenu: View = itemView.findViewById(R.id.btn_menu_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate( R.layout.item_row_post_home, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, location, image) = listPost[position]
        holder.postImage.setImageResource(image)
        holder.postTitle.text = title
        holder.postLocation.text = location

        if (binding.id == R.id.rv_posting_horizon) {
           holder.itemView.layoutParams.width = 400
            if (position == itemCount - 1){
            }
        }  else {
           holder.itemView.layoutParams.height = 1700
        }



        holder.btnMenu.setOnClickListener{
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }

    }

    override fun getItemCount(): Int = listPost.size

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }
}