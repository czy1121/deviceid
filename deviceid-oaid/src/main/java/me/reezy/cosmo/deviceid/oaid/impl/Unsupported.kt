package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider

internal class Unsupported : OaidProvider {
    override fun isSupport(context: Context): Boolean  = false

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        callback(Result.failure(OaidException("unsupported")))
    }
}