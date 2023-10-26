package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import android.net.Uri
import android.os.Build
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider

internal class Nubia : OaidProvider {

    override fun isSupport(context: Context): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        try {
            val uri = Uri.parse("content://cn.nubia.identity/identity")
            val client = context.contentResolver.acquireContentProviderClient(uri)

            val bundle = client?.call("getOAID", null, null)

            if (Build.VERSION.SDK_INT >= 24) {
                client?.close()
            } else {
                client?.release()
            }

            if (bundle == null) {
                throw OaidException("nubia call failed")
            }
            if (bundle.getInt("code", -1) != 0) {
                throw OaidException(bundle.getString("message"))
            }

            val oaid = bundle.getString("id")
            if (oaid.isNullOrBlank()) {
                throw OaidException("nubia get oaid failed")
            }
            callback(Result.success(oaid))
            return
        } catch (e: Exception) {
            callback(Result.failure(e))
        }
    }
}