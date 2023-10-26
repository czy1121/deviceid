@file:Suppress("DEPRECATION")

package me.reezy.cosmo.deviceid.oaid

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.IBinder
import android.os.Parcel
import android.util.Log


internal fun log(message: String, throwable: Throwable? = null, tag: String = "ezy") {
    Log.e(tag, message, throwable)
}

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

internal fun ContentResolver.getId(uri: String, args: Array<String>?, callback: (Result<String>) -> Unit) {
    try {
        val cursor = query(Uri.parse(uri), null, null, args, null) ?: throw OaidException("oaid cursor is null")

        val oaid = cursor.use {
            if (!it.moveToFirst()) {
                throw OaidException("oaid cursor is empty")
            }
            val index = it.getColumnIndex("value")
            if (index < 0) {
                throw OaidException("oaid cursor columnIndex out of range")
            }
           it.getString(index)
        }
        if (oaid.isNullOrBlank()) {
            throw OaidException("oaid is empty")
        }
        callback.invoke(Result.success(oaid))
    } catch (e: Exception) {
        callback(Result.failure(e))
    }
}


