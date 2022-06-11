package com.capstone.pet2home.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.capstone.pet2home.BuildConfig.BASE_URL
import com.capstone.pet2home.R
import com.capstone.pet2home.api.response.GetUserRes
import com.capstone.pet2home.databinding.FragmentProfileBinding
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.postadd.PostAddActivity
import com.capstone.pet2home.ui.profile.adapter.ProfileSectionsPagerAdapter
import com.capstone.pet2home.ui.settings.SettingsActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.dmoral.toasty.Toasty

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var totalPost: Int? = 0
    private var totalFav: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModelSetup()
        sectionPagerAdapterSetup()


        binding.fabCreatePost.setOnClickListener {
            startActivity(Intent(context, PostAddActivity::class.java))
        }

        return root
    }

    private fun sectionPagerAdapterSetup() {
        val sectionsPagerAdapter = ProfileSectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs


        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun profileViewModelSetup() {
        val profileViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore),requireContext()))[ProfileViewModel::class.java]

        profileViewModel.getUser().observe(viewLifecycleOwner){
            profileViewModel.getUserApi(it.userId, it.token)
            profileViewModel.getUserPetApi(it.userId, it.token)
        }

        profileViewModel.usersData.observe(viewLifecycleOwner){userData ->
            if(userData != null) setDataUser(userData)
        }

        profileViewModel.returnResponse.observe(viewLifecycleOwner){
            if(it.status != 200){
                Toasty.error(requireContext(),it.message, Toast.LENGTH_SHORT, true).show()
                showLoading(true)
            }
        }

        profileViewModel.countPostUser.observe(viewLifecycleOwner) { totalPost = it}

        profileViewModel.showLoading.observe(viewLifecycleOwner) {showLoading(it)}
    }

    private fun setDataUser(user: GetUserRes) {
        binding.textUserEmail.text = user.result.data[0]?.email
        binding.textUserFullName.text = user.result.data[0]?.username
        binding.textUserPost.text = totalPost.toString() //example
        binding.textUserFavorite.text = totalFav.toString() //example
        Glide.with(binding.imageUserAvatar).load(URL_AVATAR + user.result.data[0]?.avatar).circleCrop().into(binding.imageUserAvatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                Toasty.info(requireContext(),"Share Profile", Toast.LENGTH_SHORT, true).show()
                true
            }
            R.id.menu_settings -> {
                val i = Intent(activity, SettingsActivity::class.java)
                startActivity(i)
                true
            }
            else -> false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.apply {
                imageUserAvatar.visibility = View.INVISIBLE
                textUserEmail.visibility = View.INVISIBLE
                textUserFullName.visibility = View.INVISIBLE
                divider.visibility = View.INVISIBLE
                textPosting.visibility = View.INVISIBLE
                textUserPost.visibility = View.INVISIBLE
                textFavorite.visibility = View.INVISIBLE
                textUserFavorite.visibility = View.INVISIBLE
            }
        } else {
            binding.progressBar.visibility = View.GONE
            binding.apply {
                imageUserAvatar.visibility = View.VISIBLE
                textUserEmail.visibility = View.VISIBLE
                textUserFullName.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
                textPosting.visibility = View.VISIBLE
                textUserPost.visibility = View.VISIBLE
                textFavorite.visibility = View.VISIBLE
                textUserFavorite.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_post,
            R.string.tab_text_favorite
        )
        const val URL_AVATAR = BASE_URL + "public/upload/"
    }
}
