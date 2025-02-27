package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import com.hihonor.ads.identifier.AdvertisingIdClient
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.validate
import java.util.concurrent.Executors


/**
 * 参阅荣耀官方
 * [HONOR Ads SDK](https://developer.hihonor.com/cn/kitdoc?kitId=11030&navigation=guides&docId=dev-overview.md)
 */
internal class Honor : OaidProvider {

    override fun isSupport(context: Context): Boolean {
        return try {
            AdvertisingIdClient.isAdvertisingIdAvailable(context)
        } catch (e: Exception) {
            false
        }
    }

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(context)
                if (info.isLimit) {
                    throw OaidException("oaid is limited")
                }
                callback(validate(info.id))
            } catch (ex: Throwable) {
                callback(Result.failure(ex))
            }
        }
    }
}