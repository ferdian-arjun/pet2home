package com.capstone.pet2home.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pet2home.R
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.FragmentHomeBinding
import com.capstone.pet2home.helper.MarginItemDecoration
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.home.adapter.ListPostHorizontalAdapter
import com.capstone.pet2home.ui.home.adapter.ListPostVerticalAdapter
import com.capstone.pet2home.ui.postdetail.PostDetailActivity
import com.capstone.pet2home.ui.search.SearchFragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import es.dmoral.toasty.Toasty


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            btnFilterDog.isSelected = true
            btnFilterCat.isSelected = true
        }

        setupViewModel()
        bannerCarousel()
        buttonFilterByPet()
        buttonSearch()

        return root
    }

    private fun setupViewModel() {
        val homeViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore),requireContext()))[HomeViewModel::class.java]

        homeViewModel.getUser().observe(viewLifecycleOwner){
            homeViewModel.getAllPetApi(it.token)
        }

        homeViewModel.petsData.observe(viewLifecycleOwner){
            if(it != null) setHorizonPost(it)
            if(it != null) setVerticalPost(it)
        }

        homeViewModel.returnResponse.observe(viewLifecycleOwner){
            if(it.status != 200){
                Toasty.error(requireContext(),it.message, Toast.LENGTH_SHORT, true).show()
            }
        }

    }

    private fun setVerticalPost(pets: List<DataItemPet>) {
        val listPets = ArrayList<DataItemPet>()
        for (pet in pets){
            listPets.add(pet)
        }

        val adapter = ListPostVerticalAdapter(listPets, object : ListPostVerticalAdapter.OptionsMenuClickListener{

            override fun onOptionsMenuClicked(data: DataItemPet) {
                // bottomSheetDialogShow(data)
            }
        })

        adapter.setOnItemClickCallback(object : ListPostVerticalAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItemPet) {
                val intentDetail = Intent(activity, PostDetailActivity::class.java)
                intentDetail.putExtra(PostDetailActivity.EXTRA_ID_POST, data.idPost)
                startActivity(intentDetail)
            }
        })

        binding.rvPostingVertical.adapter = adapter
        showRecyclerList(binding.rvPostingVertical, LinearLayoutManager.VERTICAL)
    }

    private fun setHorizonPost(pets: List<DataItemPet>) {
        val listPets = ArrayList<DataItemPet>()
        for (pet in pets){
            listPets.add(pet)
        }

        val adapter = ListPostHorizontalAdapter(listPets, object : ListPostHorizontalAdapter.OptionsMenuClickListener{

            override fun onOptionsMenuClicked(data: DataItemPet) {
                // bottomSheetDialogShow(data)
            }
        })

        adapter.setOnItemClickCallback(object : ListPostHorizontalAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItemPet) {
                val intentDetail = Intent(activity, PostDetailActivity::class.java)
                intentDetail.putExtra(PostDetailActivity.EXTRA_ID_POST, data.idPost)
                startActivity(intentDetail)
            }
        })

        binding.rvPostingHorizon.adapter = adapter
        showRecyclerList(binding.rvPostingHorizon, LinearLayoutManager.HORIZONTAL)
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

    private fun showRecyclerList(rv: RecyclerView, orientation: Int) {
        rv.layoutManager = LinearLayoutManager(context, orientation,false)

        if (rv.id == R.id.rv_posting_horizon){
            rv.addItemDecoration(
                MarginItemDecoration(spaceSize = resources.getDimensionPixelSize(R.dimen.margin_default), orientation = orientation)
            )
        }
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