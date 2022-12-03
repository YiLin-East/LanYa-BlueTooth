package com.fc.lanya

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.permissionx.guolindev.PermissionX


class MainActivity : AppCompatActivity() {

    val TAG = "fanchao"

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 添加fragment
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.thisfragment, BlankFragment())
        transaction.commit()

        checkForLocationPermission()

    }

    /**
     * From Android 10 onwards it needs Access Location to search Bluetooth Devices
     */
    private fun checkForLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "checkForLocationPermission: 有权限权限")
            } else {
                getPermission()
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                    ), 1
//                )
            }
        }
    }

    /**
     * Request Access Location while using the App, because bluetooth need location to start discovering devices
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: 检查通过")
        } else {
            Log.i(TAG, "onRequestPermissionsResult: 没通过")
        }
    }

    fun getPermission(){
        // 获取权限
        val resquestList = ArrayList<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resquestList.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            resquestList.add(Manifest.permission.BLUETOOTH_SCAN)
            resquestList.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resquestList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }


        resquestList.add(Manifest.permission.BLUETOOTH)
        resquestList.add(Manifest.permission.BLUETOOTH_ADMIN)

        resquestList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        resquestList.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        PermissionX.init(this)
            .permissions(resquestList)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "需要这些权限", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "你需要在设置中手动赋予权限", "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "所有权限都以获取", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "以下权限已被拒绝: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }

}