package com.devyash.workingwithcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devyash.workingwithcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val TAG: String = "SUSPENDINGPRAC"

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

        CoroutineScope(Dispatchers.Main).launch {
            task1()
        }

        CoroutineScope(Dispatchers.Main).launch {
            task2()
        }

        CoroutineScope(Dispatchers.IO).launch {
            printFollowers()
        }

        CoroutineScope(Dispatchers.IO).launch {
            printIntaFollowers()
        }

        CoroutineScope(Dispatchers.IO).launch {
            jobHierarchy()
        }
        CoroutineScope(Dispatchers.IO).launch {
            jobCancellation()
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
            for (i in 1..1000000L) {

            }
        }
    }

    private fun useCorountineScope() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                Log.d("THREADNAME", Thread.currentThread().name) // This will run on main thread
            }
            Log.d("THREADNAME", Thread.currentThread().name)
        }
    }

    //Suspending funtions
    suspend fun task1() {
        Log.d(TAG, "Starting Task 1")
        yield()
        Log.d(TAG, "Ending Task 1")
    }

    suspend fun task2() {
        Log.d(TAG, "Starting Task 2")
        yield()
        Log.d(TAG, "Ending Task 2")
    }

    private suspend fun printFollowers() {
        var followers = 0
        val job = CoroutineScope(Dispatchers.IO).launch {
            followers = getFollowers()
        }
        job.join()
        Log.d(TAG, followers.toString())
    }

    private suspend fun getFollowers(): Int {
        delay(1000)
        return 100
    }

    private suspend fun printIntaFollowers() {
        val deferredJob = GlobalScope.async(Dispatchers.IO) {
            getInstaFollowers()
        }
        val res = deferredJob.await()
        Log.d(TAG, "INSTA FOLLOWERS:- ${res.toString()}")
    }

    private suspend fun getInstaFollowers(): Int {
        delay(1000)
        return 1000
    }

    private suspend fun jobHierarchy() {
        val parentJob = GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "PARENT:- $coroutineContext")

            val childJob = launch(Dispatchers.IO) {
                Log.d(TAG, "CHILD:- $coroutineContext")
            }
        }


        val parentJob2 = GlobalScope.launch(Dispatchers.Main) {
            Log.d("JOBHIERARCHY", "Parent Job Started")

            val childJob = launch(Dispatchers.IO) {
                Log.d("JOBHIERARCHY", "Child Job Started")
                delay(5000)
                Log.d("JOBHIERARCHY", "Child Job Ended")
            }
            delay(3000)
            Log.d("JOBHIERARCHY", "Parent Job Ended")
        }

        parentJob2.join()
        Log.d("JOBHIERARCHY", "Parent Job Completed")
    }

    private suspend fun jobCancellation() {
        val parentJob = GlobalScope.launch(Dispatchers.IO) {
            for (i in 1..1000) {
                if (isActive) {
                    startLongRunningTask()
                    Log.i("CANCEL", i.toString())
                }
            }
        }

        delay(100)
        Log.d("CANCEL", "Canceling Job")
        parentJob.cancel()
        parentJob.join()
        Log.d("CANCEL", "Parent Completed")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}