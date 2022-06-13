package com.capstone.pet2home.ui.profile

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
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.pet2home.R
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.FragmentProfilePostBinding
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.postdetail.PostDetailActivity
import com.capstone.pet2home.ui.postedit.PostEditActivity
import com.capstone.pet2home.ui.profile.adapter.ListPostUserAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty


class ProfilePostFragment : Fragment() {
    private var _binding: FragmentProfilePostBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentProfilePostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModelSetup()

        return root
    }

    private fun profileViewModelSetup() {
        val profileViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore),requireContext()))[ProfileViewModel::class.java]

        profileViewModel.getUser().observe(viewLifecycleOwner){
            profileViewModel.getUserPetApi(it.userId, it.token)
        }

        profileViewModel.userPetData.observe(viewLifecycleOwner) {
            showLoading(false)
            if (it != null){
                setPostingUser(it)
            }
        }
    }

    private fun setPostingUser(pets: List<DataItemPet>) {
        val listPets = ArrayList<DataItemPet>()
        for (pet in pets){
            listPets.add(pet)
        }

        isNoContent(listPets.isEmpty())

        val adapter = ListPostUserAdapter(listPets, object : ListPostUserAdapter.OptionsMenuClickListener{

            override fun onOptionsMenuClicked(data: DataItemPet) {
                bottomSheetDialogShow(data)
            }
        })

        adapter.setOnItemClickCallback(object : ListPostUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItemPet) {
                val intentDetail = Intent(activity, PostDetailActivity::class.java)
                intentDetail.putExtra(PostDetailActivity.EXTRA_ID_POST, data.idPost)
                startActivity(intentDetail)
            }
        })

        binding.rvPosting.adapter = adapter
        binding.rvPosting.layoutManager = GridLayoutManager(context, 2)
    }

    private fun bottomSheetDialogShow(data: DataItemPet) {
        val profileViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore),requireContext()))[ProfileViewModel::class.java]
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_menu_post,null)
        val dialogMenu = BottomSheetDialog(requireContext())
        dialogMenu.setContentView(view)
        dialogMenu.show()

        val btnEdit: View = view.findViewById(R.id.btn_menu_edit)
        val btnShare: View = view.findViewById(R.id.btn_menu_share)
        val btnDelete: View = view.findViewById(R.id.btn_menu_delete)

        btnEdit.setOnClickListener {
            val intent = Intent(activity, PostEditActivity::class.java)
            intent.putExtra(PostEditActivity.EXTRA_ID_POST,data.idPost)
            startActivity(intent)
        }

        btnShare.setOnClickListener {
            Toasty.success(requireContext(), "shere ${data.idPost}", Toast.LENGTH_LONG,true).show();
        }

        btnDelete.setOnClickListener {

            profileViewModel.getUser().observe(this){
                profileViewModel.deletePostApi(postId = data.idPost, token = it.token)
            }

            profileViewModel.returnResponse.observe(this){
                activity?.recreate()
                if(it.status == 200){
                    dialogMenu.hide()

                    Alerter.create(requireActivity())
                        .setTitle(getString(R.string.success))
                        .setText(getString(R.string.successfully_deleted))
                        .setBackgroundColorRes(R.color.teal_200)
                        .setDuration(1500)
                        .setIcon(R.drawable.ic_info_24)
                        .show()


                }else{
                    if(profileViewModel.showLoading.value == false){
                        Alerter.create(requireActivity())
                            .setTitle(getString(R.string.failed))
                            .setText(it.message)
                            .setBackgroundColorRes(R.color.red)
                            .setDuration(1500)
                            .setIcon(R.drawable.ic_error)
                            .show()
                    }
                    dialogMenu.hide()
                }
            }
        }
    }

    private fun isNoContent(isNot: Boolean) {
        if(isNot){
            binding.apply {
                ivNoContent.visibility=View.VISIBLE
                tvNoContent.visibility=View.VISIBLE
            }
        } else {
            binding.apply {
                ivNoContent.visibility = View.GONE
                tvNoContent.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        internal val TAG = ProfilePostFragment::class.java.simpleName
        const val ARG_SECTION_NUMBER = "section_number"

    }
}