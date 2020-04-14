package com.p.hotspotpoc

import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mReservation: LocalOnlyHotspotReservation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var a = ApManager.isApOn(this) // check Ap state :boolean
        Log.d("state",a.toString())

        checkStatus()

        ActivityCompat.requestPermissions(this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1
        )

        btnOn.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
               mReservation= ApManager.configApStateOreo(this)
            }else {
                ApManager.configApState(this)
            }
            txtStatus.text="Hotspot is On"}
        btnOff.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                ApManager.close()
            }else {
                ApManager.configApState(this)
            }

            txtStatus.text="Hotspot is Off" }
    }

    private fun checkStatus() {
        if(ApManager.isApOn(this)){
            txtStatus.text="Hotspot is On"
        }else{
            txtStatus.text="Hotspot is Off"
        }
    }
}
