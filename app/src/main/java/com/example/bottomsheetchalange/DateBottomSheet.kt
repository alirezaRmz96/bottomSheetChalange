package com.example.bottomsheetchalange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bottomsheetchalange.databinding.DateBottomSheetFramentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class DateBottomSheet(
    private val dateHolder: DatePickerHolder,
    private val onDateSelected: DateBottomSheet.(atePickerInput: DatePickerInput) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: DateBottomSheetFramentBinding
    private val viewModel by viewModels<DateBottomSheetViewModel>()

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


        datePicker(dateHolder = dateHolder)

        binding.btnDone.setOnClickListener {
            onDateSelected.invoke(
                this, DatePickerInput(
                    year = dateHolder.selectedDate.value.year,
                    month = dateHolder.selectedDate.value.month,
                    day = dateHolder.selectedDate.value.day
                )
            )
        }
    }

    private fun datePicker(
        dateHolder: DatePickerHolder
    ) {

        Log.d("alireza", "datePicker: ${dateHolder.selectedDate.value.year}")
        lifecycleScope.launch {
            launch {
                dateHolder.maxMonthIntList.collect {
                    setUpMonth(maxMonthInList = it)
                }
            }
            launch {
                dateHolder.maxDayIntList.collect {
                    setUpDay(maxDayInList = it)
                }
            }

        }
        // year
        binding.yearPicker.apply {
            minValue = dateHolder.minYearIntList.value
            maxValue = dateHolder.maxYearIntList.value
            value = dateHolder.selectedDate.value.year
            setOnValueChangedListener { _, _, newVal ->
                dateHolder.updateYear(newVal)
            }
            wrapSelectorWheel = false
        }

    }

    private fun setUpMonth(maxMonthInList: Int?) {
        val minMonth = dateHolder.minMonthIntList.value
        val maxMonth = maxMonthInList ?: dateHolder.selectedDate.value.month
        val displayedValue = dateHolder.persianMonthNames.copyOfRange(minMonth - 1, maxMonth)

        if (viewModel.maxMonth != maxMonth || viewModel.minMonth != minMonth) {
            viewModel.maxMonth = maxMonth
            viewModel.minMonth = minMonth
            binding.monthPicker.apply {
                minValue = dateHolder.minMonthIntList.value
                maxValue = maxMonth
                value = dateHolder.selectedDate.value.month
                displayedValues = displayedValue
                setOnValueChangedListener { _, _, newVal ->
                    dateHolder.updateMonth(newVal)
                }
                wrapSelectorWheel = false
            }
        } else {
            binding.monthPicker.apply {
                minValue = dateHolder.minMonthIntList.value
                maxValue = 12
                value = dateHolder.selectedDate.value.month
                displayedValues = dateHolder.persianMonthNames
                setOnValueChangedListener { _, _, newVal ->
                    dateHolder.updateMonth(newVal)
                }
                wrapSelectorWheel = false
            }
        }


    }

    private fun setUpDay(maxDayInList: Int?) {
        binding.dayPicker.apply {
            minValue = dateHolder.minDayIntList.value
            maxValue = maxDayInList ?: dateHolder.maxDayIntList.value
            value = dateHolder.selectedDate.value.day
            setOnValueChangedListener { _, _, newVal ->
                dateHolder.updateDay(newVal)
            }
            wrapSelectorWheel = false
        }
    }

}