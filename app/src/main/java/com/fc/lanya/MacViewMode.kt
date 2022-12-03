package com.fc.lanya

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MacViewMode : ViewModel() {
    val nameAndMac = MutableLiveData<String>()
    val state = MutableLiveData<String>()

    fun upData (HashMap : String){
        nameAndMac.postValue(HashMap)
        Log.i("fanchao", "在viewMode中修改: $HashMap")
    }
}