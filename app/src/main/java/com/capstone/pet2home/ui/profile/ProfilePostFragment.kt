package com.capstone.pet2home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.FragmentProfilePostBinding
import com.capstone.pet2home.model.Post
import com.capstone.pet2home.ui.postadd.editpost.EditPostActivity
import com.capstone.pet2home.ui.profile.adapter.ListPostUserAdapter

class ProfilePostFragment : Fragment() {
    private var _binding: FragmentProfilePostBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvPostings: RecyclerView
    private val list = ArrayList<Post>()

    private val listPosting: ArrayList<Post> get() {
        val dataPostTitle = resources.getStringArray(R.array.data_post_title)
        val dataPostLocation = resources.getStringArray(R.array.data_post_location)
        val dataPhotoImage = resources.obtainTypedArray(R.array.data_post_image)

        val listPost = ArrayList<Post>()
        for (i in dataPostTitle.indices) {
            val hero = Post(dataPostTitle[i],dataPostLocation[i], dataPhotoImage.getResourceId(i, -1))
            listPost.add(hero)
        }
        return listPost
    }

    private fun showRecyclerList() {
        binding.rvPosting.layoutManager = LinearLayoutManager(context)
        val listHeroAdapter = ListPostUserAdapter(list, object : ListPostUserAdapter.OptionsMenuClickListener{
            override fun onOptionsMenuClicked(position: Int) {
                performOptionsMenuClick(position)
            }
        })
        binding.rvPosting.adapter = listHeroAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentProfilePostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvPostings = binding.rvPosting
        rvPostings.setHasFixedSize(true)

        list.addAll(listPosting)
        showRecyclerList()

        rvPostings.layoutManager = GridLayoutManager(context, 2)

        return root
    }

    private fun performOptionsMenuClick(position: Int) {
        val popupMenu = PopupMenu(context, binding.rvPosting[position].findViewById(R.id.btn_menu_option))

        popupMenu.inflate(R.menu.card_view_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.edit_post -> {
                    val intent = Intent(activity, EditPostActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.delete_post -> {
                    Toast.makeText(context, "${list[position].title} delete", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}