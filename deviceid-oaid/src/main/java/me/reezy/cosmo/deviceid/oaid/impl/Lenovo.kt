package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.content.Intent
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.OaidService
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage

internal class Lenovo : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasPackage("com.zui.deviceidservice")


    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val intent = Intent()
        intent.setClassName("com.zui.deviceidservice", "com.zui.deviceidservice.DeviceidService")

        OaidService.bind(context, intent, callback) {
            it.getId("com.zui.deviceidservice.IDeviceidInterface", 1)
        }
    }
}