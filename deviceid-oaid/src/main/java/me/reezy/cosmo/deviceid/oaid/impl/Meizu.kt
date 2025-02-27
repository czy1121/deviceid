package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.hasContentProvider
import me.reezy.cosmo.deviceid.oaid.queryId

internal class Meizu : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasContentProvider("com.meizu.flyme.openidsdk")

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        context.queryId("content://com.meizu.flyme.openidsdk/", arrayOf("oaid"), callback)
    }
}