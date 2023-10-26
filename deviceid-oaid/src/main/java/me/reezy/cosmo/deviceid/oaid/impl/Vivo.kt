package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.Rom
import me.reezy.cosmo.deviceid.oaid.getId

internal class Vivo : OaidProvider {

    override fun isSupport(context: Context): Boolean {
        return Rom.getSystemProperty("persist.sys.identifierid.supported", "0") == "1"
    }

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        context.contentResolver.getId("content://com.vivo.vms.IdProvider/IdentifierId/OAID", null, callback)
    }
}