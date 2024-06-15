package com.example.bottomsheetchalange

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Sms
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat.registerReceiver
import com.example.bottomsheetchalange.databinding.FragmentBBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class BFragment : Fragment() {

    private lateinit var binding: FragmentBBinding
    private val smsVerificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                val extras = intent.extras
                val smsReceiverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsReceiverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            getLauncher.launch(consentIntent)
                        } catch (_: Exception) {
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> {}
                }
            }
        }

    }

    private val getLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data = result.data
            if (result.resultCode == Activity.RESULT_OK && data != null) {

                val message =
                    data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)

                val otp = message?.filter { it.isDigit() }

                binding.etBFragment.setText(otp)
            } else {
                Log.d("TAG", "onReceive: Permission denied ")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBBinding.inflate(
            inflater, container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireContext().registerReceiver(
            smsVerificationReceiver,
            intentFilter,
            SmsRetriever.SEND_PERMISSION,
            null
        )
        SmsRetriever.getClient(requireActivity())
            .startSmsUserConsent(null)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Log.d("TAG", "onResume: Successfully")
                else
                    Log.d("TAG", "onResume: failed")
            }
    }
}