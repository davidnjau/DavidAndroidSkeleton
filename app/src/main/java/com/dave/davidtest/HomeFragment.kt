package com.dave.davidtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.dave.davidtest.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var fragmentHomeBinding : FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding = binding
        binding.tvHome.text = "Test Data"
        binding.btnNext.setOnClickListener {
            val bundle = bundleOf("id" to "1")
            view.findNavController().navigate(R.id.profileFragment,bundle)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        fragmentHomeBinding = null
        super.onDestroyView()
    }

}