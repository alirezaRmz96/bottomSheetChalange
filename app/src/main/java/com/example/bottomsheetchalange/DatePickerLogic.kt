package com.example.bottomsheetchalange

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import saman.zamani.persiandate.PersianDate


class DatePickerHolder(
    private val minDate: DatePickerInput? = null,
    private val maxDate: DatePickerInput? = null,
    private val currentSelectedDate: DatePickerInput
) {

    var minDayIntList = MutableStateFlow(1)
    var maxDayIntList = MutableStateFlow(31)

    var minMonthIntList = MutableStateFlow(1)
    var maxMonthIntList = MutableStateFlow(12)

    var minYearIntList = MutableStateFlow(minDate?.year ?: (currentSelectedDate.year - 20))
    var maxYearIntList = MutableStateFlow(maxDate?.year ?: currentSelectedDate.year)

    val availableDays = combine(minDayIntList, maxDayIntList) { _, _ ->
        (minDayIntList.value..maxDayIntList.value).map { it.toString() }.toTypedArray()
    }

    val availableMonths = combine(minMonthIntList, maxMonthIntList) { _, _ ->
        (minMonthIntList.value..maxMonthIntList.value).toList()
    }

    val availableYears = combine(minYearIntList, maxYearIntList) { _, _ ->
        (minYearIntList.value..maxYearIntList.value).map { it.toString() }.toTypedArray()
    }

    var selectedDate = MutableStateFlow(currentSelectedDate)
        private set

    init {
        setConstraints()
    }

    private fun setConstraints() {
        val newSelectedDate = MutableStateFlow(selectedDate.value.copy())

        val minMonth = minDate
            .takeIf { it?.year == selectedDate.value.year }
            ?.month
            ?: 1

        val maxMonth = maxDate
            .takeIf { it?.year == selectedDate.value.year }
            ?.month
            ?: 12

        val minDay = minDate
            .takeIf { it?.year == selectedDate.value.year && it.month >= selectedDate.value.month }
            ?.day
            ?: 1

        val maxDay = maxDate
            .takeIf { it?.year == selectedDate.value.year && it.month <= selectedDate.value.month }
            ?.day
            ?: getNumberOfDaysInMonth(
                year = selectedDate.value.year,
                month = selectedDate.value.month
            )

        newSelectedDate.value = when {

            selectedDate.value.month == minMonth && selectedDate.value.day < minDay ->
                newSelectedDate.value.copy(
                    day = minDay
                )

            selectedDate.value.month < minMonth ->
                newSelectedDate.value.copy(
                    month = minMonth,
                    day = minDay
                )

            selectedDate.value.month == maxMonth && selectedDate.value.day > maxDay ->
                newSelectedDate.value.copy(
                    day = maxDay
                )

            selectedDate.value.month > maxMonth ->
                newSelectedDate.value.copy(
                    month = maxMonth,
                    day = maxDay
                )

            else -> newSelectedDate.value
        }

        minDayIntList.value = minDay
        maxDayIntList.value = maxDay
        minMonthIntList.value = minMonth
        maxMonthIntList.value = maxMonth
        selectedDate = newSelectedDate
    }

    fun updateDay(day: Int) {
        selectedDate.value = selectedDate.value.copy(day = day)
    }

    fun updateMonth(month: Int) {
        selectedDate.value = selectedDate.value.copy(month = month)
        setConstraints()
    }

    fun updateYear(year: Int) {
        selectedDate.value = selectedDate.value.copy(year = year)
        setConstraints()
    }

    val persianMonthNames = arrayOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند",
    )
}


data class DatePickerInput(
    val year: Int,
    val month: Int,
    val day: Int
) {
    fun toStringDate(divider: Char = '/'): String = "$year$divider$month$divider$day"
}


fun getNumberOfDaysInMonth(year: Int, month: Int): Int {
    val persianDate = PersianDate()
    persianDate.setShYear(year)
    persianDate.setShMonth(month)

    return persianDate.monthDays
}