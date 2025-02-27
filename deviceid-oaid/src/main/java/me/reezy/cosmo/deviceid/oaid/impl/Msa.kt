package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.OaidConnection
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage


internal class Msa : OaidProvider {
    override fun isSupport(context: Context): Boolean = context.hasPackage("com.mdid.msa")


    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        try {
            val intent = Intent("com.bun.msa.action.start.service")
            intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaKlService")
            intent.putExtra("com.bun.msa.param.pkgname", context.packageName)
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        } catch (e: java.lang.Exception) {
            callback(Result.failure(e))
        }

        val intent = Intent("com.bun.msa.action.bindto.service")
        intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaIdService")
        intent.putExtra("com.bun.msa.param.pkgname", context.packageName)

        OaidConnection.bind(context, intent, callback) {
            it.getId("com.bun.lib.MsaIdInterface", 2)
        }
    }
}