package com.capstone.pet2home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.FragmentHomeBinding
import com.capstone.pet2home.helper.MarginItemDecoration
import com.capstone.pet2home.model.Post
import com.capstone.pet2home.ui.home.adapter.ListPostAdapter
import com.capstone.pet2home.ui.search.SearchFragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val list = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bannerCarousel()
        postContent()
        buttonFilterByPet()
        buttonSearch()

        return root
    }

    private fun buttonSearch() {
       binding.btnSearch.setOnClickListener {
           val mSearchFragment = SearchFragment()
           val mFragmentManager = parentFragmentManager
           mFragmentManager.beginTransaction().apply {
               add(R.id.nav_host_fragment_activity_main, mSearchFragment, SearchFragment::class.java.simpleName)
               addToBackStack(null)
               commit()
           }
       }
    }

    private fun buttonFilterByPet() {

        binding.btnFilterDog.setOnClickListener { it ->
            it.isSelected = it.isSelected == false
        }

        binding.btnFilterCat.setOnClickListener { it ->
            it.isSelected = it.isSelected == false
        }
    }

    private fun postContent() {
        list.addAll(listPosting)

        showRecyclerList(binding.rvPostingHorizon, LinearLayoutManager.HORIZONTAL)
        showRecyclerList(binding.rvPostingVertical, LinearLayoutManager.VERTICAL)
    }

    private val listPosting: ArrayList<Post> get() {
        //data sample
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

    private fun showRecyclerList(rv: RecyclerView, orientation: Int) {
        rv.layoutManager = LinearLayoutManager(context, orientation,false)
        rv.adapter = ListPostAdapter(list, object : ListPostAdapter.OptionsMenuClickListener{
            override fun onOptionsMenuClicked(position: Int) {
              //  performOptionsMenuClick(position)
            }
        }, rv)

        rv.addItemDecoration(
           MarginItemDecoration(spaceSize = resources.getDimensionPixelSize(R.dimen.margin_default), orientation = orientation)
        )
    }


    private fun bannerCarousel() {
        val slideImages = ArrayList<SlideModel>()
        //Sample data
        slideImages.add(SlideModel("https://source.unsplash.com/1500x500/?pet"))
        slideImages.add(SlideModel("https://source.unsplash.com/1500x500/?paw"))
        slideImages.add(SlideModel("https://source.unsplash.com/1500x500/?petshop"))

        binding.imageSlider.setImageList(slideImages, ScaleTypes.FIT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}