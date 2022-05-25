package com.capstone.pet2home.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Selection.setSelection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.FragmentSearchBinding
import com.capstone.pet2home.ui.home.HomeFragment


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]


        buttonBack()

        return root
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