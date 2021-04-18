package com.app.wifiscanwithbroadcast

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    var wifim: WifiManager? = null
    var wifire:WifiReceiver?= null
    var listAdapter: WifiAdapter? = null
    var myWifiLiT: List<ScanResult>? = null
    var mySSID: List<String>? = null
    var myListView: ListView? = null
    var mytoolb: Toolbar? = null
    var contexto: Activity? = null
    var mySwitch: SwitchCompat? = null
    var myWifiList: List<Wifi>? = null
    val PERMISO_UBICACION = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myListView = findViewById(R.id.myListView)
        mytoolb = findViewById(R.id.toolb)

        wifim = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifire =WifiReceiver()
        mySSID = ArrayList()

        myWifiList = ArrayList()
        contexto = this


        setSupportActionBar(mytoolb)
        mytoolb?.setTitle(R.string.app_name)
        mytoolb?.setTitleTextColor(Color.WHITE)

        registerReceiver(wifire, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        myListView?.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val myDialog = CustomDialog(this@MainActivity, myWifiList?.get(i))
            myDialog.show(supportFragmentManager, "")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.app_bar_switch)
        mySwitch = item.actionView as SwitchCompat
        mySwitch!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                if (!VerificarPermisos(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(
                        contexto!!,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        0
                    )
                } else {
                    if (VerificarUbicacion()) {
                        ActivarWifi()
                        ScanWifiList()
                    } else PedirUbicacion()
                }
            } else {
                if ((if (listAdapter != null) listAdapter!!.count else 0) > 0) {
                    myWifiList=null
                    listAdapter!!.notifyDataSetChanged()
                    myListView!!.adapter = listAdapter
                }
                DesactivarWIfi()
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun VerificarPermisos(permiso: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            permiso!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun PedirUbicacion() {
        val lm =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                PERMISO_UBICACION
            )
        }
    }

    private fun ScanWifiList() {
        myWifiList = ArrayList()
        Toast.makeText(contexto, "Buscando...", Toast.LENGTH_LONG).show()
        Handler(Looper.myLooper()!!).postDelayed({
            wifim!!.startScan()
            myWifiLiT = wifim!!.scanResults
            for (i in (myWifiLiT as MutableList<ScanResult>?)?.indices!!) {
                (myWifiList as ArrayList<Wifi>)?.add(Wifi(myWifiLiT?.get(i)?.SSID, myWifiLiT?.get(i)?.BSSID))
            }
            listAdapter = WifiAdapter(contexto, myWifiList)
            myListView!!.adapter = listAdapter
        }, 4000)
    }

    class WifiReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && permissions[0]
                .contains(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PedirUbicacion()
            } else {
                mySwitch!!.isChecked = false
                Toast.makeText(this, "Debes permitir la Ubicación Primero", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISO_UBICACION) {
            if (VerificarUbicacion()) {
                ActivarWifi()
                ScanWifiList()
            } else {
                Toast.makeText(this, "Activar Ubicación Primero", Toast.LENGTH_LONG).show()
                DesactivarWIfi()
            }
        }
    }

    private fun VerificarUbicacion(): Boolean {
        val lm =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(lm)
    }

    private fun ActivarWifi() {
        if (!wifim!!.isWifiEnabled) wifim!!.isWifiEnabled = true
    }

    private fun DesactivarWIfi() {
        if (wifim!!.isWifiEnabled) wifim!!.isWifiEnabled = false
    }
}