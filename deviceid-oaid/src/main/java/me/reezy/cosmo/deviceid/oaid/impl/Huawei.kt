package me.reezy.cosmo.deviceid.oaid.impl

import android.content.Context
import com.huawei.hms.ads.identifier.AdvertisingIdClient
import me.reezy.cosmo.deviceid.oaid.OaidException
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.hasPackage


internal class Huawei : OaidProvider {

    override fun isSupport(context: Context): Boolean  {
        try {
            Class.forName("com.huawei.hms.ads.identifier.AdvertisingIdClient")
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return context.hasPackage("com.huawei.hwid", "com.huawei.hwid.tv", "com.huawei.hms")
    }

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val info = AdvertisingIdClient.getAdvertisingIdInfo(context)

        if (info.isLimitAdTrackingEnabled) {
            callback(Result.failure(OaidException("limited")))
        } else {
            callback(Result.success(info.id))
        }
    }
}