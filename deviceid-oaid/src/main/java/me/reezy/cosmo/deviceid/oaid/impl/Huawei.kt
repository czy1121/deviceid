package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import com.huawei.hms.ads.identifier.AdvertisingIdClient
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.validate
import java.util.concurrent.Executors

/**
 * 参阅华为官方文档
 * [HUAWEI Ads SDK](https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/identifier-service-integrating-sdk-0000001056460552)
 */
internal class Huawei : OaidProvider {

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
                if (info.isLimitAdTrackingEnabled) {
                    throw OaidException("oaid is limited")
                }
                callback(validate(info.id))
            } catch (ex: Throwable) {
                callback(Result.failure(ex))
            }
        }
    }
}