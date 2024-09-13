package com.example.bottomsheetchalange

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomsheetchalange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fromThisDate.setOnClickListener {
            DateBottomSheet(
                dateHolder = DatePickerHolder(
                    minDate = viewModel.startDate.value,
                    maxDate = viewModel.endDate.value,
                    currentSelectedDate = viewModel.currentDate.value
                        ?: DatePickerInput(
                            year = viewModel.currentYear,
                            month = viewModel.currentMonth,
                            day = viewModel.currentDay
                        ),
                ),
                onDateSelected = {
                    binding.showFromThisDate.text = it.toStringDate('/')
                    this.dismiss()
                    viewModel.updateStartDate(it.toStringDate('/'))
                }
            ).show(
                supportFragmentManager, null
            )
        }

        binding.toThisDate.setOnClickListener {
            DateBottomSheet(
                dateHolder = DatePickerHolder(
                    minDate = viewModel.startDate.value,
                    maxDate = viewModel.endDate.value,
                    currentSelectedDate = viewModel.currentDate.value
                        ?: DatePickerInput(
                            year = viewModel.currentYear,
                            month = viewModel.currentMonth,
                            day = viewModel.currentDay
                        ),
                ),
                onDateSelected = {
                    binding.showFromThisDate.text = it.toStringDate('/')
                    this.dismiss()
                    viewModel.updateStartDate(it.toStringDate('/'))
                }
            ).show(
                supportFragmentManager, null
            )
        }
    }
}