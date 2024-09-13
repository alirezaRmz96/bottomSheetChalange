package com.example.bottomsheetchalange

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomsheetchalange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomSheet.setOnClickListener {
            DateBottomSheet().show(
                supportFragmentManager,null
            )
        }
    }

    fun convertToStandardDate(year: String, month: String, day: String): String {
        val standardMonth = if (month.length == 1) "0$month" else month
        val standardDay = if (day.length == 1) "0$day" else day

        return "$year/$standardMonth/$standardDay"
    }

    //date
    fun getYearFromDate(date: String = "1370") =
        date.subSequence(0, date.indexOf("/")).toString().toInt()

    fun getMonthFromDate(date: String) =
        date.subSequence(date.indexOf("/") + 1, date.lastIndexOf("/")).toString().toInt()

    fun getDayFromDate(date: String) =
        date.subSequence(date.lastIndexOf("/") + 1, date.length).toString().toInt()
}