package com.fc.lanya

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fc.lanya.databinding.FragmentBlankBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mBluetoothFilter: IntentFilter? = null
    private var mBluetoothReceiver: BluetoothReceiver? = null

    private var button: Button? = null
    private var button2: Button? = null
    private var textview: TextView? = null
    public var Mac: MacViewMode? = null
    private var bind : FragmentBlankBinding? = null
    var fragment = this

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Mac = ViewModelProvider(requireActivity()).get(MacViewMode::class.java)
        bind = FragmentBlankBinding.inflate(inflater)
        return bind!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.button)
        button2 = view.findViewById(R.id.button2)
    }

    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        button?.setOnClickListener {
            if (bluetoothAdapter.isEnabled){
                Toast.makeText(activity,"蓝牙已开启",Toast.LENGTH_LONG).show()
            } else {
                bluetoothAdapter.enable()
            }
        }

        // 关闭蓝牙
        bind?.button3?.setOnClickListener {
            if (!bluetoothAdapter.isEnabled) {
                Toast.makeText(activity,"蓝牙已关闭",Toast.LENGTH_SHORT).show()
            } else {
                bluetoothAdapter.disable()
            }
        }

        button2?.setOnClickListener {
            if (bluetoothAdapter.isEnabled) { //打开
                //开始扫描周围的蓝牙设备,如果扫描到蓝牙设备，通过广播接收器发送广播
                bluetoothAdapter.startDiscovery()
            } else {
                Toast.makeText(activity,"请打开蓝牙",Toast.LENGTH_LONG).show()
            }
            getBlueTooth()
        }

        // 设备信息
        Mac?.nameAndMac?.observe(this){it ->
            bind?.textView2?.text = it
        }

        // 扫描状态
        Mac?.state?.observe(this){it->
            bind?.textView3?.text = it
        }
    }


    fun getBlueTooth(){
        // 输出蓝牙列表
        if (!checkBluetoothEnable()) {
            // 动态注册
            if (mBluetoothReceiver == null) {
                mBluetoothReceiver = BluetoothReceiver(Mac)
            }
            if (mBluetoothFilter == null) {
                mBluetoothFilter = BluetoothReceiver.registerIntentFilter()
            }
            if (mBluetoothReceiver != null && mBluetoothFilter != null) {
                activity?.registerReceiver(mBluetoothReceiver, mBluetoothFilter)
            }
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}