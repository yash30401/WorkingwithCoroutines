package com.devyash.workingwithcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devyash.workingwithcoroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIncreaseCounter.setOnClickListener {
            updateCounter()
        }

    }

    private fun updateCounter() {
        Log.d("THREADNAME",Thread.currentThread().name)
        var count = binding.tvCounter.text.toString().toInt()
        count++
        binding.tvCounter.text = count.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}