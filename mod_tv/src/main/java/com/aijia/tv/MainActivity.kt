package com.aijia.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.aijia.battery.BatteryView
import com.aijia.wifi.WifiView

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBatteryView()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }
    }

    private fun initBatteryView() {
        val batteryView = findViewById<BatteryView>(R.id.batteryView)
        batteryView.setLifecycleOwner(this)
        //batteryView.setOnBatteryPowerListener {  }

        val wifiView = findViewById<WifiView>(R.id.iv_wifi)
        wifiView.setLifecycleOwner(this)
    }
}