package com.capstone.pet2home.ui.profile.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.pet2home.ui.profile.ProfileFavoriteFragment
import com.capstone.pet2home.ui.profile.ProfileFragment
import com.capstone.pet2home.ui.profile.ProfilePostFragment

class ProfileSectionsPagerAdapter(activity: ProfileFragment): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ProfilePostFragment()
            1 -> fragment = ProfileFavoriteFragment()
        }
        return fragment as Fragment
    }
}