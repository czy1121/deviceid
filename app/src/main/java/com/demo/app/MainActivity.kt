package com.demo.app

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.demo.app.databinding.ActivityMainBinding
import me.reezy.cosmo.deviceid.DeviceId
import me.reezy.cosmo.deviceid.DeviceInfo
import me.reezy.cosmo.deviceid.oaid.Oaid

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    val binding by lazy { ActivityMainBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        Oaid.get(this) {
            val provider = Oaid.getProviderName(this)
            val isSupport = Oaid.isSupport(this)
            val oaid = it.getOrNull() ?: it.exceptionOrNull()?.message
            binding.oaid.text = "oaid provider = $provider\noaid support = $isSupport\noaid = $oaid"
            Log.e("OoO", binding.oaid.text.toString())
        }

        val androidId = DeviceId.getAndroidId(this)
        val widevineId = DeviceId.getWidevineId()
        val instanceId = DeviceId.getInstanceId(this)

        binding.ids.text = "androidId = $androidId\n\nwidevineId = $widevineId\n\ninstanceId = $instanceId"

        binding.system.text = DeviceInfo.get(this).format()

    }


    private fun Map<String, String>.format(): String {
        return toList().joinToString("\n") { "${it.first} = ${it.second}" }
    }
}