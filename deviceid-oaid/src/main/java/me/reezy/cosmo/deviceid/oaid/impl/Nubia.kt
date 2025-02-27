package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.net.Uri
import android.os.Build
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.validate

internal class Nubia : OaidProvider {

    override fun isSupport(context: Context): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        try {
            val uri = Uri.parse("content://cn.nubia.identity/identity")
            val bundle = context.contentResolver.acquireContentProviderClient(uri)?.use {
                it.call("getOAID", null, null)
            } ?: throw OaidException("nubia fail to get")

            if (bundle.getInt("code", -1) != 0) {
                throw OaidException(bundle.getString("message"))
            }
            val oaid = bundle.getString("id")
            callback(validate(oaid))
        } catch (e: Exception) {
            callback(Result.failure(e))
        }
    }
}