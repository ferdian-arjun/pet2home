package com.capstone.pet2home.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.pet2home.R
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.ItemPostBinding
import com.capstone.pet2home.helper.withDateFormat
import com.capstone.pet2home.ui.postedit.PostEditActivity
import com.capstone.pet2home.ui.profile.ProfileFragment

class SearchAdapter(
    private val listPost: ArrayList<DataItemPet>,
    private var onOptionsMenuClicked: OptionsMenuClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitlePost: TextView= binding.tvTitlePost
        val tvCreateTimePost: TextView= binding.tvCreateTimePost
        val tvDescription: TextView= binding.tvDescriptionPost
        val imagePost: ImageView= binding.imgItemPhotoPost
        val imageAvatarUser: ImageView= binding.ivAvatarPost
        val fullNameUser: TextView= binding.tvUsernamePost
        val btnMenuOption: View= binding.btnMenuOption
        val btnFav: View= binding.btnFavorite
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder=
        ViewHolder(ItemPostBinding.inflate(LayoutInflater.from(viewGroup.context),
            viewGroup,
            false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {
            tvTitlePost.text = listPost[position].title
            tvCreateTimePost.text = listPost[position].createdAt.withDateFormat()
            tvDescription.text = listPost[position].description
            Glide.with(itemView.context).load(PostEditActivity.URL_IMAGE + listPost[position].pic).into(imagePost)
            //           Glide.with(itemView.context).load("https://source.unsplash.com/720x600/?pet").into(imagePost)
            if(listPost[position].avatar == null || listPost[position].avatar == "default.png"){
                Glide.with(itemView.context).load(ProfileFragment.URL_AVATAR + "image_default.jpg").circleCrop().into(imageAvatarUser)
            }else{
                Glide.with(itemView.context).load(ProfileFragment.URL_AVATAR + listPost[position].avatar).circleCrop().into(imageAvatarUser)
            }

            fullNameUser.text = listPost[position].username

            imagePost.setOnClickListener {
                onItemClickCallback.onItemClicked(listPost[holder.adapterPosition])
            }

            imagePost.setOnLongClickListener {
                onOptionsMenuClicked.onOptionsMenuClicked(listPost[holder.adapterPosition])
                true
            }

            btnMenuOption.setOnClickListener {
                onOptionsMenuClicked.onOptionsMenuClicked(listPost[holder.adapterPosition])
            }

            btnFav.setOnClickListener {
                btnFav.background = ContextCompat.getDrawable(it.context, R.drawable.ic_favorite)
            }

        }
    }

    override fun getItemCount(): Int = listPost.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(data: DataItemPet)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemPet)
    }
}