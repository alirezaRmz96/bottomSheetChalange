package com.example.bottomsheetchalange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bottomsheetchalange.databinding.DateBottomSheetFramentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import saman.zamani.persiandate.PersianDate

class DateBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: DateBottomSheetFramentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DateBottomSheetFramentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val persianDate = PersianDate()
        val currentYear = persianDate.shYear
        val currentMonth = persianDate.shMonth
        val currentDay = persianDate.shDay

        setupNumberPickers(currentYear, currentMonth, currentDay)

        binding.monthPicker.wrapSelectorWheel = false
        binding.yearPicker.wrapSelectorWheel = false
        binding.dayPicker.wrapSelectorWheel = false

        binding.yearPicker.setOnValueChangedListener { _, _, newVal ->
            updateMonthAndDayPickers(newVal, currentYear, currentMonth, currentDay)
        }

        binding.monthPicker.setOnValueChangedListener { _, _, newVal ->
            updateDayPicker(newVal, binding.yearPicker.value, currentYear, currentMonth, currentDay)
        }

        binding.dayPicker.setOnValueChangedListener { _, _, _ ->
            // No action needed here for now
        }


    }

    private fun setupNumberPickers(
        currentYear: Int,
        currentMonth: Int, currentDay: Int
    ) = kotlin.runCatching {
        // Set up the year picker
        binding.yearPicker.minValue = currentYear - 100
        binding.yearPicker.maxValue = currentYear
        binding.yearPicker.value = currentYear

        // Set up the month picker with names
        val persianMonths = arrayOf(
            "Farvardin",
            "Ordibehesht",
            "Khordad",
            "Tir",
            "Mordad",
            "Shahrivar",
            "Mehr",
            "Aban",
            "Azar",
            "Dey",
            "Bahman",
            "Esfand"
        )
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = currentMonth
        binding.monthPicker.displayedValues = persianMonths
        binding.monthPicker.value = currentMonth

        // Set up the day picker
        updateDayPicker(currentMonth, currentYear, currentYear, currentMonth, currentDay)
    }

    private fun updateMonthAndDayPickers(
        selectedYear: Int,
        currentYear: Int,
        currentMonth: Int,
        currentDay: Int
    ) = kotlin.runCatching {
        val persianMonths = arrayOf(
            "Farvardin",
            "Ordibehesht",
            "Khordad",
            "Tir",
            "Mordad",
            "Shahrivar",
            "Mehr",
            "Aban",
            "Azar",
            "Dey",
            "Bahman",
            "Esfand"
        )

        if (selectedYear == currentYear) {
            binding.monthPicker.maxValue = currentMonth
            binding.monthPicker.displayedValues = persianMonths.copyOfRange(0, currentMonth)
            if (binding.monthPicker.value > currentMonth) {
                binding.monthPicker.value = currentMonth
            }
        } else {
            binding.monthPicker.maxValue = 12
            binding.monthPicker.displayedValues = persianMonths
        }

        updateDayPicker(
            binding.monthPicker.value,
            selectedYear,
            currentYear,
            currentMonth,
            currentDay
        )
    }

    private fun updateDayPicker(
        selectedMonth: Int,
        selectedYear: Int,
        currentYear: Int,
        currentMonth: Int,
        currentDay: Int
    ) = kotlin.runCatching {
        val maxDay = when (selectedMonth) {
            1, 2, 3, 4, 5, 6 -> 31
            7, 8, 9, 10, 11 -> 30
            12 -> if (selectedYear % 4 == 3) 30 else 29
            else -> 31
        }

        if (selectedYear == currentYear) {
            if (selectedMonth == currentMonth) {
                binding.dayPicker.maxValue = currentDay
                binding.dayPicker.minValue = 1
                binding.dayPicker.value = currentDay
            } else if (selectedMonth < currentMonth) {
                binding.dayPicker.maxValue = maxDay
                binding.dayPicker.minValue = 1
            }
        } else {
            binding.dayPicker.maxValue = maxDay
            binding.dayPicker.minValue = 1
        }
    }

}