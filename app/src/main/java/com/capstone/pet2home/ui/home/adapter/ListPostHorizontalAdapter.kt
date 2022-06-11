package com.capstone.pet2home.ui.home.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.ItemRowPostHomeBinding
import com.capstone.pet2home.helper.checkDistance
import com.capstone.pet2home.helper.convertMeterToKilometer
import com.capstone.pet2home.helper.roundOffDecimal
import com.capstone.pet2home.ui.profile.ProfileFragment

class ListPostHorizontalAdapter(
    private val listPost: ArrayList<DataItemPet>,
    private var onOptionsMenuClicked: OptionsMenuClickListener,
    private var latlonCurrent: Array<Double>,
) : RecyclerView.Adapter<ListPostHorizontalAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(binding: ItemRowPostHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitlePost: TextView = binding.tvTitlePost
        val tvLocation: TextView = binding.tvLocationPost
        val imagePost: ImageView = binding.imgItemPhoto
        val btnMenuOption: View= binding.btnMenuOption
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder=
        ViewHolder(ItemRowPostHomeBinding.inflate(LayoutInflater.from(viewGroup.context),
            viewGroup,
            false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {

            val distance = checkDistance(latlonCurrent[0], latlonCurrent[1],listPost[position].lat,listPost[position].lon).convertMeterToKilometer()
            listPost[holder.adapterPosition].distance = distance.toString()
            tvTitlePost.text = listPost[position].title
            tvLocation.text = distance.toString() + "Km"

            Glide.with(itemView.context).load(ProfileFragment.URL_AVATAR + listPost[position].pic).into(imagePost)
           // Glide.with(itemView.context).load("https://source.unsplash.com/720x600/?pet").into(imagePost)

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listPost[holder.adapterPosition])
            }

            itemView.setOnLongClickListener {
                onOptionsMenuClicked.onOptionsMenuClicked(listPost[holder.adapterPosition])
                true
            }

            btnMenuOption.setOnClickListener {
                onOptionsMenuClicked.onOptionsMenuClicked(listPost[holder.adapterPosition])
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