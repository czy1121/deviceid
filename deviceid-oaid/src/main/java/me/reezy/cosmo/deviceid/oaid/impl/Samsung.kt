package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.content.Intent
import me.reezy.cosmo.deviceid.oaid.OaidConnection
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage

internal class Samsung : OaidProvider {
    override fun isSupport(context: Context): Boolean = context.hasPackage("com.samsung.android.deviceidservice")

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val intent = Intent()
        intent.setClassName("com.samsung.android.deviceidservice", "com.samsung.android.deviceidservice.DeviceIdService")


        OaidConnection.bind(context, intent, callback) {
            it.getId("com.samsung.android.deviceidservice.IDeviceIdService", 1)
        }
    }
}