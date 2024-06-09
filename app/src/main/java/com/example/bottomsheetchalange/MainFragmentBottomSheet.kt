package com.example.bottomsheetchalange

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomsheetchalange.databinding.BottomSheetMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainFragmentBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetMainBinding
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

        binding.apply {
            bButton.setOnClickListener {
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container,
                        BFragment()
                    )
                    .addToBackStack(null)
                    .commit()
            }
            aButton.setOnClickListener {
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container,
                        AFragment()
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}