package com.capstone.pet2home.ui.search

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pet2home.R
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.FragmentSearchBinding
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.home.HomeFragment
import com.capstone.pet2home.ui.home.HomeViewModel
import com.capstone.pet2home.ui.register.RegisterActivity
import com.capstone.pet2home.ui.search.adapter.SearchAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        super.onActivityCreated(savedInstanceState)



        setupViewModel()
        buttonBack()

        return root
    }

    private fun setupViewModel() {

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->

            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                actionSearch()
                true
            }
        false
        }

    }

    private fun actionSearch() {
        val  searchViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore),requireContext()))[SearchViewModel::class.java]

        searchViewModel.getUser().observe(viewLifecycleOwner){
            searchViewModel.searchPost(it.token , binding.etSearch.text.toString())
        }

        searchViewModel.petsData.observe(viewLifecycleOwner){
            if(it != null) {
                binding.tvNoContent.visibility = View.GONE
                setDataSearch(it)
            }else{
                binding.tvNoContent.visibility = View.VISIBLE
            }
        }

    }

    private fun setDataSearch(pets: List<DataItemPet>) {
        val dataPost = ArrayList<DataItemPet>()
        for (pet in pets){
            dataPost.add(pet)
        }

        val adapter = SearchAdapter(dataPost,object : SearchAdapter.OptionsMenuClickListener{
            override fun onOptionsMenuClicked(data: DataItemPet) {
                TODO("Not yet implemented")
            }

        })

        adapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItemPet) {
                TODO("Not yet implemented")
            }
        })

        binding.rvSearching.adapter
        showRecyclerList(binding.rvSearching, LinearLayoutManager.VERTICAL)
    }

    private fun showRecyclerList(rv: RecyclerView, orientation: Int) {
        rv.layoutManager = LinearLayoutManager(context, orientation,false)
    }

    private fun showSoftKeyboard(view: View) {
        view.requestFocus()
        if (view.requestFocus()) {
            (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(view, SHOW_IMPLICIT)

        }
    }

    override fun onResume() {
        super.onResume()
        showSoftKeyboard(binding.etSearch)
    }

    private fun buttonBack() {
        binding.btnBack.setOnClickListener {
            val mHomeFragment = HomeFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment_activity_main, mHomeFragment, HomeFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
    
    
}