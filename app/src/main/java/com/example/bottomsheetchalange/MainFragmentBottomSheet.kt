package com.example.bottomsheetchalange

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.bottomsheetchalange.databinding.BottomSheetMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMainBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetMainBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController


        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event?.action == KeyEvent.ACTION_UP) {
                        if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
                            navController.navigateUp()
                        } else {
                            dismiss()
                        }
                    }
                    return true
                }
                return false
            }
        })

        dialog?.setOnDismissListener {
            dismiss()
        }
    }

}