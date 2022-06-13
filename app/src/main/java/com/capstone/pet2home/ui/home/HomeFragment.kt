package com.capstone.pet2home.ui.home

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
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
import com.capstone.pet2home.helper.checkDistance
import com.capstone.pet2home.helper.convertMeterToKilometer
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.home.adapter.ListPostHorizontalAdapter
import com.capstone.pet2home.ui.home.adapter.ListPostVerticalAdapter
import com.capstone.pet2home.ui.postdetail.PostDetailActivity
import com.capstone.pet2home.ui.search.SearchFragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import es.dmoral.toasty.Toasty
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var latLon: Array<Double>? = arrayOf(0.0,0.0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            btnFilterDog.isSelected = true
            btnFilterCat.isSelected = false
        }
        getCurrentLocation()

      //  setupViewModel()
        bannerCarousel()
        buttonFilterByPet()
        buttonSearch()

        return root
    }

    private fun getCurrentLocation() {
        showLoading(true)
        val locationRequeest = LocationRequest()
        locationRequeest.interval = 10000
        locationRequeest.fastestInterval = 50000
        locationRequeest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var addresses:List<Address>


//        LocationServices.getFusedLocationProviderClient(requireContext())
//            .lastLocation.addOnCompleteListener(requireActivity()){ task ->
//                val location: Location? = task.result
//                if(location != null) {
//                    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                    latLon = arrayOf(addresses[0].latitude,addresses[0].latitude)
//                    val address:String = addresses[0].locality
//                    binding.tvLocation.text = address
//                    if(latLon == null){
//                        latLon = arrayOf(0.0,0.0)
//                    }
//                    setupViewModel()
//                }
//            }


        LocationServices.getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(locationRequeest,object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(requireContext())
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0){
                        val locIndex = locationResult.locations.size-1

                        val latitude = locationResult.locations[locIndex].latitude
                        val longitude = locationResult.locations[locIndex].longitude

                        addresses = geocoder.getFromLocation(latitude,longitude,1)

                        latLon = arrayOf(latitude,longitude)
                        val address:String = addresses[0].locality
                        binding.tvLocation.text = address
                        if(latLon != null){
                            setupViewModel()
                        }else{
                            setupViewModel()
                        }
                     //   activity?.recreate()
                    }
                }
            }, Looper.getMainLooper())
    }

    private fun setupViewModel() {
        val homeViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore),requireContext()))[HomeViewModel::class.java]

        homeViewModel.getUser().observe(viewLifecycleOwner){
            homeViewModel.getAllPetApi(it.token)
        }

        homeViewModel.petsData.observe(viewLifecycleOwner){
            if(it != null) setHorizonPost(it)
            if(it != null) setVerticalPost(it)
            showLoading(false)
        }

        homeViewModel.returnResponse.observe(viewLifecycleOwner){
            if(it.status != 200){
                Toasty.error(requireContext(),it.message, Toast.LENGTH_LONG, true).show()
                binding.tvWaitingData.text = it.message
                binding.progressBar.visibility = View.GONE
            }
        }

        showLoading(false)

    }

    private fun setVerticalPost(pets: List<DataItemPet>) {
        val listPetsVertical = ArrayList<DataItemPet>()
        for (pet in pets){
            pet.distance =  checkDistance(latLon?.get(0),latLon?.get(1),pet.lat,pet.lon).convertMeterToKilometer().toString()
            listPetsVertical.add(pet)
        }

        listPetsVertical.sortByDescending {
            it.createdAt
        }

        val adapter = ListPostVerticalAdapter(listPetsVertical, object : ListPostVerticalAdapter.OptionsMenuClickListener{

            override fun onOptionsMenuClicked(data: DataItemPet) {
                // bottomSheetDialogShow(data)
            }
        })

        adapter.setOnItemClickCallback(object : ListPostVerticalAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItemPet) {
                val intentDetail = Intent(activity, PostDetailActivity::class.java)
                intentDetail.putExtra(PostDetailActivity.EXTRA_ID_POST, data.idPost)
                intentDetail.putExtra(PostDetailActivity.EXTRA_DISTANCE, data.distance)
                startActivity(intentDetail)
            }
        })

        binding.rvPostingVertical.adapter = adapter
        showRecyclerList(binding.rvPostingVertical, LinearLayoutManager.VERTICAL)
    }

    private fun setHorizonPost(pets: List<DataItemPet>) {
        val listPetsHorizon = ArrayList<DataItemPet>()
        for (pet in pets){
            val distancePost = checkDistance(latLon?.get(0),latLon?.get(1),pet.lat,pet.lon).convertMeterToKilometer()
            pet.distance = distancePost.toString()
            // ambil data < 1 Km
            if(distancePost < 5){
                listPetsHorizon.add(pet)
            }
        }

        if(listPetsHorizon.isEmpty()){
            binding.tvNoPostAroundYou.visibility = View.VISIBLE
            binding.ivNoPostAroundYou.visibility = View.VISIBLE
        }else{
            binding.tvNoPostAroundYou.visibility = View.GONE
            binding.ivNoPostAroundYou.visibility = View.GONE
        }

        listPetsHorizon.sortByDescending {
            it.createdAt
        }

        val adapter = ListPostHorizontalAdapter(listPetsHorizon, object : ListPostHorizontalAdapter.OptionsMenuClickListener{

            override fun onOptionsMenuClicked(data: DataItemPet) {
                // bottomSheetDialogShow(data)
            }
        }, latLon!!)

        adapter.setOnItemClickCallback(object : ListPostHorizontalAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItemPet) {
                val intentDetail = Intent(activity, PostDetailActivity::class.java)
                intentDetail.putExtra(PostDetailActivity.EXTRA_ID_POST, data.idPost)
                intentDetail.putExtra(PostDetailActivity.EXTRA_DISTANCE, data.distance)
                startActivity(intentDetail)
            }
        })

        binding.rvPostingHorizon.adapter = adapter
        showRecyclerList(binding.rvPostingHorizon, LinearLayoutManager.HORIZONTAL)
       // binding.rvPostingHorizon.addItemDecoration(MarginItemDecoration(spaceSize = 40, orientation = LinearLayoutManager.HORIZONTAL))
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
//            rv.addItemDecoration(
//                MarginItemDecoration(spaceSize = 40, orientation = orientation)
//            )
        }
    }


    private fun bannerCarousel() {
        val slideImages = ArrayList<SlideModel>()
        //Sample data
        slideImages.add(SlideModel("https://imgur.com/2Ka6PaN.png"))
        slideImages.add(SlideModel("https://imgur.com/ctSBm4o.png"))
        slideImages.add(SlideModel("https://imgur.com/CisDWDA.png"))

        binding.imageSlider.setImageList(slideImages, ScaleTypes.FIT)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvWaitingData.visibility = View.VISIBLE
            binding.layoutContent.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.tvWaitingData.visibility = View.GONE
            binding.layoutContent.visibility = View.VISIBLE
        }
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