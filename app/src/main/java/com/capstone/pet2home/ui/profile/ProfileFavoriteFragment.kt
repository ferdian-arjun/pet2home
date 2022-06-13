package com.capstone.pet2home.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.capstone.pet2home.databinding.FragmentProfileFavoriteBinding

class ProfileFavoriteFragment : Fragment() {
    private var _binding: FragmentProfileFavoriteBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentProfileFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        isNoContent(true)

        return root
    }

    private fun isNoContent(isNot: Boolean) {
       if(isNot){
           binding.ivNoContentFavorite.visibility = View.VISIBLE
           binding.tvNoContentFavorite.visibility = View.VISIBLE
       }else{
           binding.ivNoContentFavorite.visibility = View.GONE
           binding.tvNoContentFavorite.visibility = View.GONE
       }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}