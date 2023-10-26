package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.content.Intent
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.OaidService
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage

internal class Freeme : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasPackage("com.android.creator")


    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val intent = Intent("android.service.action.msa",)
        intent.setPackage("com.android.creator")

        OaidService.bind(context, intent, callback) {
            it.getId("com.android.creator.IdsSupplier", 3)
        }
    }
}