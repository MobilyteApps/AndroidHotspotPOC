package com.p.hotspotpoc

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mReservation: LocalOnlyHotspotReservation? = null
    //////////////////////device admin ////////////
    private var activeDevicePolicyManager: DevicePolicyManager? = null
    private val LOG_TAG = "ActiveDevicePolicy"
    var mDPM: DevicePolicyManager? = null
    var mAdminName: ComponentName? = null
    /////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var a = ApManager.isApOn(this) // check Ap state :boolean
        Log.d("state", a.toString())
        checkStatus()
        // Crashlytics.getInstance().crash()
        makeDeviceOwner()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.CHANGE_NETWORK_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE
            ), 1
        )

        btnOn.setOnClickListener {

            checkLocation()

        }

        btnOff.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ApManager.close()
            } else {
                ApManager.configApState(applicationContext)
            }

            txtStatus.text = "Hotspot is Off"
        }
    }

    private fun makeDeviceOwner() {
        mDPM =
            getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mAdminName = ComponentName(this, DeviceAdminSample::class.java)
        if (!mDPM!!.isAdminActive(mAdminName)) { // try to become active – must happen here in this activity, to get result
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(
                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mAdminName
            )
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why this needs to be added."
            )
            startActivityForResult(intent, 123)
        }
//        try {
//            Runtime.getRuntime()
//                .exec("dpm set-device-owner com.p.hotspotpoc/.DeviceAdminSample")
//        } catch (e: java.lang.Exception) {
//            Log.e("FragmentActivity.TAG", "device owner not set")
//            Log.e("FragmentActivity.TAG", e.toString())
//            e.printStackTrace()
//        }
    }

    private fun checkLocation() {
        val lm =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!gps_enabled && !network_enabled) { // notify user
            AlertDialog.Builder(this)
                .setMessage("Gps not enabled")
                .setPositiveButton(
                    "open settings",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    })
                .setNegativeButton("cancel", null)
                .show()
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mReservation = ApManager.configApStateOreo(this)
            } else {
                ApManager.configApState(applicationContext)

            }
            txtStatus.text = "Hotspot is On"
        }
    }

    private fun checkStatus() {
        if (ApManager.isApOn(this)) {
            txtStatus.text = "Hotspot is On"
        } else {
            txtStatus.text = "Hotspot is Off"
        }
    }

}
