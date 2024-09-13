package com.example.bottomsheetchalange

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import saman.zamani.persiandate.PersianDate

class MainViewModel : ViewModel() {
    private val persianDate = PersianDate()
    private val startDateStr = "1393/01/01".split("/").map { it.toInt() }

    val currentYear = persianDate.shYear
    val currentMonth = persianDate.shMonth
    val currentDay = persianDate.shDay

    private val _endDate = MutableLiveData(
        DatePickerInput(
            year = currentYear,
            month = currentMonth,
            day = currentDay
        )
    )
    val endDate get() = _endDate

    private val _startDate = MutableLiveData(
        DatePickerInput(
            year = startDateStr[0],
            month = startDateStr[1],
            day = startDateStr[2],
        )
    )
    val startDate get() = _startDate

    private val _currentDate = MutableLiveData(
        DatePickerInput(
            year = currentYear,
            month = currentMonth,
            day = currentDay
        )
    )
    val currentDate get() = _currentDate


    fun updateCurrentDate(selectedDate: String) {
        val dateParts = selectedDate.split("/").map { it.toInt() }
        _startDate.value = DatePickerInput(
            year = dateParts[0],
            month = dateParts[1],
            day = dateParts[2],
        )
    }

    fun updateStartDate(selectedDate: String) {
        val dateParts = selectedDate.split("/").map { it.toInt() }
        _startDate.value = DatePickerInput(
            year = dateParts[0],
            month = dateParts[1],
            day = dateParts[2],
        )
        _currentDate.value = DatePickerInput(
            year = dateParts[0],
            month = dateParts[1],
            day = dateParts[2],
        )

    }

}