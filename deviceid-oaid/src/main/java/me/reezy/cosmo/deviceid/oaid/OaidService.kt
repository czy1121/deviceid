package me.reezy.cosmo.deviceid.oaid

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Parcel

class OaidService private constructor(val context: Context, val callback: (Result<String>) -> Unit, val getter: (IBinder) -> String?) : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        log("$name onServiceConnected")
        try {
            val oaid = getter.invoke(service)

            if (oaid.isNullOrBlank()) {
                throw OaidException("$name get failed")
            }
            callback(Result.success(oaid))
        } catch (e: Exception) {
            callback(Result.failure(e))
        } finally {
            try {
                context.unbindService(this)
            } catch (ex: Throwable) {
                //
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
        log("$name onServiceDisconnected")
    }

    companion object {
        fun bind(context: Context, intent: Intent, callback: (Result<String>) -> Unit, getter: (IBinder) -> String?) {
            try {
                val isBound = context.bindService(intent, OaidService(context.applicationContext, callback, getter), Context.BIND_AUTO_CREATE)
                if (!isBound) {
                    throw RuntimeException("${intent.component} bind service failed")
                }
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }
}