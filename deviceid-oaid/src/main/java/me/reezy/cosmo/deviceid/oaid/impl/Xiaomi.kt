package me.reezy.cosmo.deviceid.oaid.impl

import android.annotation.SuppressLint
import android.content.Context
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.validate


@SuppressLint("PrivateApi")
internal class Xiaomi : OaidProvider {
    private var idProviderClazz: Class<*>? = null

    override fun isSupport(context: Context): Boolean {
        if (idProviderClazz == null) {
            try {
                idProviderClazz = Class.forName("com.android.id.impl.IdProviderImpl")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return  idProviderClazz != null
    }

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        try {
            val clazz = idProviderClazz!!
            val method = clazz.getMethod("getOAID", Context::class.java)

            val oaid = method.invoke(clazz.newInstance(), context) as String?

            callback(validate(oaid))
        } catch (e: Exception) {
            callback(Result.failure(e))
        }
    }
}