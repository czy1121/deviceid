package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.content.Intent
import me.reezy.cosmo.deviceid.oaid.OaidConnection
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage
import me.reezy.cosmo.deviceid.oaid.isLimited

internal class Gms : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasPackage("com.android.vending")


    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val intent = Intent("com.google.android.gms.ads.identifier.service.START")
        intent.setPackage("com.google.android.gms")

        OaidConnection.bind(context, intent, callback) {
            val interfaceName = "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService"
            if (it.isLimited(interfaceName, 2)) {
                throw OaidException("oaid is limited")
            }
            it.getId(interfaceName, 1)
        }
    }
}