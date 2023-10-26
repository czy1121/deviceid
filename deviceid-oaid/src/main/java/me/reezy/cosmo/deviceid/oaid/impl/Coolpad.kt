package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.content.Intent
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.OaidService
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage

internal class Coolpad : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasPackage("com.coolpad.deviceidsupport")


    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val intent = Intent()
        intent.setClassName("com.coolpad.deviceidsupport", "com.coolpad.deviceidsupport.DeviceIdService")

        OaidService.bind(context, intent, callback) {
            it.getId("com.coolpad.deviceidsupport.IDeviceIdManager", 2) {
                writeString(context.packageName)
            }
        }
    }
}