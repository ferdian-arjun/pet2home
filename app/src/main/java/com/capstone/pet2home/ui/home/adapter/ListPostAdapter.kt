package com.capstone.pet2home.ui.home.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.pet2home.R
import com.capstone.pet2home.formatDate
import com.capstone.pet2home.model.Post
import com.capstone.pet2home.ui.postdetail.PostDetailActivity
import java.util.*
import kotlin.collections.ArrayList

class ListPostAdapter(
    private val listPost: ArrayList<Post>,
    private val binding: RecyclerView
) : RecyclerView.Adapter<ListPostAdapter.ListViewHolder>() {

  //  private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView = itemView.findViewById(R.id.img_item_photo_post)
        var postTitle: TextView = itemView.findViewById(R.id.tv_title_post)
        var btnMenu: View = itemView.findViewById(R.id.btn_menu_option)
        var postLocation: TextView = itemView.findViewById(R.id.tv_location_post)
        var postCreateTIme: TextView = itemView.findViewById(R.id.tv_create_time_post)
        var postdescription: TextView = itemView.findViewById(R.id.tv_description_post)
        var postAvatar: ImageView = itemView.findViewById(R.id.iv_avatar_post)
        var postUsername: TextView = itemView.findViewById(R.id.tv_username_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            when(binding.id){
                R.id.rv_posting_horizon -> LayoutInflater.from(parent.context).inflate( R.layout.item_row_post_home, parent, false)
                else -> LayoutInflater.from(parent.context).inflate( R.layout.item_post, parent, false)
            }
        return ListViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = listPost[position]
        Glide.with(holder.itemView.context)
            .load(post.pic)
            .into(holder.postImage)
        holder.postTitle.text = post.tittle
        holder.postLocation.text = post.location
        holder.postdescription.text = post.description
        holder.postCreateTIme.text = formatDate(post.createdAt, TimeZone.getDefault().id)


        if (binding.id == R.id.rv_posting_horizon) {
           holder.itemView.layoutParams.width = 400
            if (position == itemCount - 1){
            }
        }  else {
//           holder.itemView.layoutParams.height = 1700
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
            intent.putExtra(PostDetailActivity.DETAIL_POST, post)
        }


        holder.btnMenu.setOnClickListener{

        }

    }

    /*fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }*/

    override fun getItemCount(): Int = listPost.size

   /* interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Post)
    }*/
}