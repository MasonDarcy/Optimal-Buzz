package com.example.optimal_buzz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.optimal_buzz.fragments.SessionFragment
import com.example.optimal_buzz.fragments.SessionViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var session: SessionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            session = supportFragmentManager.getFragment(savedInstanceState, "myFragmentName") as SessionFragment;
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("MyBoolean", true)
        savedInstanceState.putDouble("myDouble", 1.9)
        savedInstanceState.putInt("MyInt", 1)
        savedInstanceState.putString("MyString", "Welcome back to Android")
        // etc.
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        val myBoolean = savedInstanceState.getBoolean("MyBoolean")
        val myDouble = savedInstanceState.getDouble("myDouble")
        val myInt = savedInstanceState.getInt("MyInt")
        val myString = savedInstanceState.getString("MyString")
    }

    override fun onStart() {
        super.onStart()

        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }



}
