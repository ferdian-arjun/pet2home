package com.capstone.pet2home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.FragmentProfileBinding
import com.capstone.pet2home.ui.postadd.PostAddActivity
import com.capstone.pet2home.ui.profile.adapter.ProfileSectionsPagerAdapter
import com.capstone.pet2home.ui.settings.SettingsActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textUserFullName
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        val sectionsPagerAdapter = ProfileSectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs


        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabCreatePost.setOnClickListener {
            startActivity(Intent(context, PostAddActivity::class.java))
        }

        return root
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
                Toast.makeText(context , "Share Profile" , Toast.LENGTH_SHORT).show()
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_post,
            R.string.tab_text_favorite
        )
    }
}