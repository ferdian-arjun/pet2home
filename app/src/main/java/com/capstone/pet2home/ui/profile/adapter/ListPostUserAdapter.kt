package com.capstone.pet2home.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.ItemRowPostBinding
import com.capstone.pet2home.helper.distanceInKm
import com.capstone.pet2home.helper.roundOffDecimal
import com.capstone.pet2home.helper.withDateFormat
import com.capstone.pet2home.ui.profile.ProfileFragment


class ListPostUserAdapter(
    private val listPost: ArrayList<DataItemPet>,
    private var onOptionsMenuClicked: OptionsMenuClickListener
) : RecyclerView.Adapter<ListPostUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback


    class ViewHolder(binding: ItemRowPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitlePost: TextView = binding.tvTitlePost
        val imagePost: ImageView = binding.imagePost
        val tvDatePost: TextView = binding.tvDatePost
        val btnMenuOption: View= binding.btnMenuOption
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRowPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {
            tvTitlePost.text = listPost[position].title
            tvDatePost.text = listPost[position].createdAt.withDateFormat()
            Glide.with(itemView.context).load(ProfileFragment.URL_AVATAR + listPost[position].pic).into(imagePost)
           // Glide.with(itemView.context).load("https://source.unsplash.com/720x600/?pet").into(imagePost)

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listPost[position])
            }

            itemView.setOnLongClickListener {
                onOptionsMenuClicked.onOptionsMenuClicked(listPost[position])
                true
            }

            btnMenuOption.setOnClickListener {
                onOptionsMenuClicked.onOptionsMenuClicked(listPost[position])
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