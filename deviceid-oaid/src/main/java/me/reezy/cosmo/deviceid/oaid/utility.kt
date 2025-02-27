@file:Suppress("DEPRECATION")

package me.reezy.cosmo.deviceid.oaid

import android.content.Context
import android.net.Uri
import android.os.IBinder
import android.os.Parcel


internal fun Context.hasPackage(vararg packageNames: String) = try {
    packageNames.any { packageManager.getPackageInfo(it, 0) != null }
} catch (e: Exception) {
    e.printStackTrace()
    false
}

internal fun Context.hasContentProvider(name: String) = try {
    packageManager.resolveContentProvider(name, 0) != null
} catch (e: Exception) {
    e.printStackTrace()
    false
}

internal fun IBinder.isLimited(interfaceName: String, code: Int, paramsBlock: (Parcel.() -> Unit)? = null): Boolean {
    val data = Parcel.obtain()
    val reply = Parcel.obtain()
    return try {
        data.writeInterfaceToken(interfaceName)
        paramsBlock?.invoke(data)
        transact(code, data, reply, 0)
        reply.readException()
        reply.readInt() != 0
    } finally {
        data.recycle()
        reply.recycle()
    }
}

internal fun IBinder.getId(interfaceName: String, code: Int, paramsBlock: (Parcel.() -> Unit)? = null): String? {
    val data = Parcel.obtain()
    val reply = Parcel.obtain()
    return try {
        data.writeInterfaceToken(interfaceName)
        paramsBlock?.invoke(data)
        transact(code, data, reply, 0)
        reply.readException()
        reply.readString()
    } finally {
        data.recycle()
        reply.recycle()
    }
}

internal fun Context.queryId(uri: String, args: Array<String>?, callback: (Result<String>) -> Unit) {
    try {
        val cursor = contentResolver.query(Uri.parse(uri), null, null, args, null)
            ?: throw OaidException("cursor is null")

        val oaid = cursor.use {
            if (!it.moveToFirst()) {
                throw OaidException("cursor is empty")
            }
            val index = it.getColumnIndex("value")
            if (index < 0) {
                throw OaidException("cursor columnIndex out of range")
            }
            it.getString(index)
        }
        callback(validate(oaid))
    } catch (e: Exception) {
        callback(Result.failure(e))
    }
}

internal fun validate(oaid: String?) = when {
    oaid.isNullOrBlank() -> Result.failure(OaidException("oaid is empty"))
    oaid.matches(Regex("^[0-]+$")) -> Result.failure(OaidException("oaid is invalid"))
    else -> Result.success(oaid)
}

