package com.example.bottomsheetchalange

object DateTimeUtils {



    /*fun getCurrentDate(): String {
        val persianDate = PersianDateImpl()
        val year = persianDate.persianYear.toString()
        val month = persianDate.persianMonth.toString()
        val day = persianDate.persianDay.toString()

        return convertToStandardDate(year, month, day)
    }

    fun getTodayDate(): String {
        val persianDate = PersianDate()
        val dayName = persianDate.dayName()
        val day = persianDate.shDay
        val monthName = persianDate.monthName()
        return "$dayName $day $monthName"
    }

    fun getTomorrowDate() = getDateByDiff(getCurrentDate(), 1)

    private fun getDateByDiff(date: String, diff: Int): String {
        val jalaliCalendar = JalaliCalendar(
            getYearFromDate(date),
            getMonthFromDate(date),
            getDayFromDate(date)
        )
        val nextDate = jalaliCalendar.getDateByDiff(diff)

        return convertToStandardDate(
            nextDate.year.toString(),
            nextDate.month.toString(),
            nextDate.day.toString()
        )
    }



    private fun convertMonthToDay(startDate: String, periodNumber: Int): Int {

        val currentMonth = getMonthFromDate(startDate)
        var day = 0

        val jalaliCalendar = JalaliCalendar(
            getYearFromDate(startDate),
            getMonthFromDate(startDate),
            getDayFromDate(startDate)
        )

        for (i in currentMonth until currentMonth + periodNumber) {
            val month = if (i <= 12) i else i % 12
            jalaliCalendar.month = month
            day += jalaliCalendar.monthLength
        }
        return day
    }

    @SuppressLint("SimpleDateFormat")
    fun compareTwoDate(firstDate: String, secondDate: String): Int {

        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val d1: Date = dateFormat.parse(firstDate) as Date
        val d2: Date = dateFormat.parse(secondDate) as Date

        return d1.compareTo(d2)
    }

    fun updateDataAndTimeIfExpired(medicineView: MedicineView): MedicineView {
        val currentDate = getCurrentDate()
        val currentTime = getCurrentTime()
        if (compareTwoDate(medicineView.nextDate, currentDate) == -1
            || (compareTwoDate(medicineView.nextDate, currentDate) == 0
                    && compareTwoTime(
                medicineView.nextTime,
                currentTime
            ) <= 0)

        ) {
            val dateAndTime = calNextDateAndTime(
                medicineView.startDate,
                medicineView.startTime,
                medicineView.periodUnit,
                medicineView.periodNumber
            )

            medicineView.nextDate = dateAndTime.first
            medicineView.nextTime = dateAndTime.second
        }
        return medicineView
    }

    fun convertToStandardDate(data: String): String {
        if (data.length == 8) {
            val year = data.substring(0, 4)
            val month = data.substring(4, 6)
            val day = data.substring(6, 8)
            return "$year/$month/$day"
        } else {
            throw IllegalArgumentException("Input must be an 8-digit integer.")
        }
    }

    //time
    fun getHourFromTime(time: String) = time.substring(0, time.indexOf(":"))

    fun getMinuteFromTime(time: String) = time.substring(time.indexOf(":") + 1)

    fun getCurrentTime(): String {

        val calender: Calendar = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = if (calender.get(Calendar.MINUTE) < 10) {
            "0${calender.get(Calendar.MINUTE)}"
        } else {
            "${calender.get(Calendar.MINUTE)}"
        }

        return "$hour:$minute"
    }

    fun getCurrentTimeWithFiveMinuteInterval(): String {
        val currentTime = getCurrentTime()
        val hour = getHourFromTime(currentTime)
        var minute = getMinuteFromTime(currentTime).toInt()

        minute -= (minute % 5)

        return if (minute < 10)
            "$hour:0$minute"
        else
            "$hour:$minute"
    }

    @SuppressLint("SimpleDateFormat")
    fun compareTwoTime(firsTime: String, secondTime: String): Int {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val d1 = simpleDateFormat.parse(firsTime)
        val d2 = simpleDateFormat.parse(secondTime)
        if (d1 != null && d2 != null) {
            return d1.compareTo(d2)
        }
        return -1
    }

    fun calNextDateAndTime(
        date: String,
        time: String,
        periodUnit: String,
        periodNumber: Int
    ): Pair<String, String> {

        var diffDays = 0
        when (periodUnit) {
            Constants.HOUR -> {
                return calcNextDateAndTimeForHourUnit(date, time, periodNumber)
            }

            Constants.DAY -> diffDays += periodNumber
            Constants.WEEK -> diffDays += (periodNumber * 7)
            Constants.MONTH -> diffDays =
                convertMonthToDay(
                    date,
                    periodNumber
                )
        }

        val jalaliCalendar = JalaliCalendar(
            getYearFromDate(date),
            getMonthFromDate(date),
            getDayFromDate(date)
        )
        val jDate = jalaliCalendar.getDateByDiff(diffDays)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = jDate.toGregorian().timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, getHourFromTime(time).toInt())
        calendar.set(Calendar.MINUTE, getMinuteFromTime(time).toInt())
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val nextDate = convertToStandardDate(
            jDate.year.toString(),
            jDate.month.toString(),
            jDate.day.toString()
        )
        val nextTime = SimpleDateFormat("HH:mm").format(Date(calendar.timeInMillis))
        return if (Calendar.getInstance().timeInMillis > calendar.timeInMillis)
            calNextDateAndTime(nextDate, time, periodUnit, periodNumber)
        else
            Pair(nextDate, nextTime)
    }

    private fun calcNextDateAndTimeForHourUnit(
        date: String,
        time: String,
        periodNumber: Int
    ): Pair<String, String> {

        var persianDate = PersianDate().apply {
            shYear = getYearFromDate(date)
            shMonth = getMonthFromDate(date)
            shDay = getDayFromDate(date)
        }


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = persianDate.time
        calendar.set(Calendar.HOUR_OF_DAY, getHourFromTime(time).toInt())
        calendar.set(Calendar.MINUTE, getMinuteFromTime(time).toInt())
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        var nextTimeStamp = calendar.timeInMillis
        val currentCalendar = Calendar.getInstance()
        //calc next dateAndTime
        do {
            nextTimeStamp += (1000 * 60 * 60 * periodNumber)
        } while (currentCalendar.timeInMillis > nextTimeStamp)

        persianDate = PersianDate(nextTimeStamp)

        val nextTime = SimpleDateFormat("HH:mm").format(persianDate.toDate())
        val nextDate =
            convertToStandardDate(
                persianDate.shYear.toString(),
                persianDate.shMonth.toString(),
                persianDate.shDay.toString()
            )
        return Pair(nextDate, nextTime)
    }*/
}