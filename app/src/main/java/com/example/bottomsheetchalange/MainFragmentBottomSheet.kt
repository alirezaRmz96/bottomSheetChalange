package com.example.bottomsheetchalange

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.bottomsheetchalange.databinding.BottomSheetMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainFragmentBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetMainBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetMainBinding.inflate(
            inflater,container,false
        )

        return binding.root
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.apply {
            bButton.setOnClickListener {
                navController.navigate(R.id.BFragment)

            }
            aButton.setOnClickListener {
                navController.navigate(R.id.AFragment)
            }
        }
    }
}