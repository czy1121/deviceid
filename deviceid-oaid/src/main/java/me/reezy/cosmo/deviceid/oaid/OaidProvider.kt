
package me.reezy.cosmo.deviceid.oaid

import android.content.Context

interface OaidProvider {
    fun isSupport(context: Context): Boolean
    fun get(context: Context, callback: (Result<String>) -> Unit)
}