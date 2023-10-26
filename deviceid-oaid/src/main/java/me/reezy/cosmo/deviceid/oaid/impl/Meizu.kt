package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasContentProvider

internal class Meizu : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasContentProvider("com.meizu.flyme.openidsdk")

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        context.contentResolver.getId("content://com.meizu.flyme.openidsdk/", arrayOf("oaid"), callback)
    }
}