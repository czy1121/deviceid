package me.reezy.cosmo.deviceid.oaid

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class OaidConnection private constructor(val context: Context, val callback: (Result<String>) -> Unit, val getter: (IBinder) -> String?) : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        try {
            callback(validate(getter.invoke(service)))
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
    }

    companion object {
        fun bind(context: Context, intent: Intent, callback: (Result<String>) -> Unit, getter: (IBinder) -> String?) {
            try {
                val isBound = context.bindService(intent, OaidConnection(context.applicationContext, callback, getter), Context.BIND_AUTO_CREATE)
                if (!isBound) {
                    throw OaidException("fail to bound service ${intent.component}")
                }
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }
}