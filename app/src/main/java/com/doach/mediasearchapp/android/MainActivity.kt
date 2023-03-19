package com.doach.mediasearchapp.android

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.doach.mediasearchapp.android.databinding.ActivityMainBinding
import com.doach.mediasearchapp.android.presentation.home.HomeFragment
import com.doach.mediasearchapp.android.presentation.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var backPressedTime: Long = 0
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            // TODO: 좀 더 간결하게 정리 필요함
            val currentShownFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0)
            if (currentShownFragment is HomeFragment) {
                with(System.currentTimeMillis()) {
                    if (this - backPressedTime > 3000L) {
                        backPressedTime = this
                        showToast("뒤로 가기를 한번 더 누르면 앱이 종료됩니다.")
                        return
                    }
                }

                finish()
            } else {
                findNavController(R.id.nav_host_fragment).popBackStack()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

}