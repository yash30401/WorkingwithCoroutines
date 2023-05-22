package com.devyash.workingwithcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devyash.workingwithcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIncreaseCounter.setOnClickListener {
            updateCounter()
        }

        binding.btnLongTask.setOnClickListener {
            startLongRunningTask()
            useCorountineScope()
        }


    }


    private fun updateCounter() {
        Log.d("THREADNAME", Thread.currentThread().name)
        var count = binding.tvCounter.text.toString().toInt()
        count++
        binding.tvCounter.text = count.toString()
    }

    private fun startLongRunningTask() {
        thread(start = true) {
            Log.d("THREADNAME", Thread.currentThread().name)
            for (i in 1..1000000000L) {

            }
        }
    }

    private fun useCorountineScope() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("THREADNAME", Thread.currentThread().name)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}