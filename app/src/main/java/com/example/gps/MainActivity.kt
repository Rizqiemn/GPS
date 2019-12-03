package com.example.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Looper
import android.os.ProxyFileDescriptorCallback
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallBack: LocationCallback
    val REQUEST_CODE=1000;
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(
                            this@MainActivity,
                            "Permition Granted",
                            Toast.LENGTH_LONG
                        ).show()
                    else
                        Toast.makeText(
                            this@MainActivity,
                            "Permition Denied",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }
    }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
            else
                buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
            btn_start.setOnClickListener(View.OnClickListener {
                if(ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }

                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack, Looper.myLooper())
                btn_start.isEnabled= !btn_start.isEnabled
                btn_stop.isEnabled=!btn_stop.isEnabled
            });
            btn_stop.setOnClickListener(View.OnClickListener {
                if(ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }

                fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
                btn_start.isEnabled= !btn_start.isEnabled
                btn_stop.isEnabled=!btn_stop.isEnabled
            });
        }


    private fun buildLocationCallBack(){
        locationCallBack =object :LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size-1)//get last location
                text_location.text=location.latitude.toString()+"/"+location.longitude.toString()
            }
        }
    }
    private fun buildLocationRequest(){
        locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement=10f
    }

}
