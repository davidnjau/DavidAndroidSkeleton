package com.dave.davidtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dave.davidtest.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var fragmentProfileBinding : FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        fragmentProfileBinding = binding

        binding.tvUid.text = arguments?.getString("id")

    }


    override fun onDestroyView() {
        super.onDestroyView()

        fragmentProfileBinding = null
        super.onDestroyView()
    }
}