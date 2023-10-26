package me.reezy.cosmo.deviceid.oaid.impl

import android.annotation.SuppressLint
import android.content.Context
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider


@SuppressLint("PrivateApi")
internal class Xiaomi : OaidProvider {
    private var idProviderClazz: Class<*>? = null

    override fun isSupport(context: Context): Boolean {
        ensureClass()
        return  idProviderClazz != null
    }

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        ensureClass()
        val clazz = idProviderClazz ?: throw OaidException("oaid no support")
        try {
            val method = clazz.getMethod("getOAID", Context::class.java)

            val oaid = method.invoke(clazz.newInstance(), context) as String?

            if (oaid.isNullOrBlank()) {
                throw OaidException("oaid get failed")
            }
            callback(Result.success(oaid))
        } catch (e: Exception) {
            callback(Result.failure(e))
        }
    }

    private fun ensureClass() {
        if (idProviderClazz == null) {
            try {
                idProviderClazz = Class.forName("com.android.id.impl.IdProviderImpl")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}