package me.reezy.cosmo.deviceid.oaid

import android.content.Context
import me.reezy.cosmo.deviceid.oaid.impl.Asus
import me.reezy.cosmo.deviceid.oaid.impl.Coolpad
import me.reezy.cosmo.deviceid.oaid.impl.Freeme
import me.reezy.cosmo.deviceid.oaid.impl.Unsupported
import me.reezy.cosmo.deviceid.oaid.impl.Gms
import me.reezy.cosmo.deviceid.oaid.impl.Huawei
import me.reezy.cosmo.deviceid.oaid.impl.Lenovo
import me.reezy.cosmo.deviceid.oaid.impl.Meizu
import me.reezy.cosmo.deviceid.oaid.impl.Msa
import me.reezy.cosmo.deviceid.oaid.impl.Nubia
import me.reezy.cosmo.deviceid.oaid.impl.Oppo
import me.reezy.cosmo.deviceid.oaid.impl.Samsung
import me.reezy.cosmo.deviceid.oaid.impl.Vivo
import me.reezy.cosmo.deviceid.oaid.impl.Xiaomi

object Oaid {

    private lateinit var provider: OaidProvider

    var oaid: String? = null
        private set

    fun getProviderName(context: Context): String {
        if (!this::provider.isInitialized) {
            provider = createProvider(context)
        }
        return provider.javaClass.simpleName.lowercase()
    }

    fun isSupport(context: Context): Boolean {
        if (!this::provider.isInitialized) {
            provider = createProvider(context)
        }
        return provider.isSupport(context)
    }

    fun get(context: Context, callback: (Result<String>) -> Unit) {
        if (!this::provider.isInitialized) {
            provider = createProvider(context)
        }
        if (!provider.isSupport(context)) {
            callback(Result.failure(OaidException("unsupported")))
            return
        }
        oaid?.let {
            callback(Result.success(it))
            return
        }
        provider.get(context) {
            if (it.isSuccess) {
                oaid = it.getOrNull()
            }
            callback(it)
        }
    }

    private fun createProvider(context: Context): OaidProvider {
        val provider = when {
            Rom.isHuawei -> Huawei()
            Rom.isOppo || Rom.isOnePlus -> Oppo()
            Rom.isVivo -> Vivo()
            Rom.isXiaomi || Rom.isBlackShark -> Xiaomi()
            Rom.isLenovo || Rom.isMotolora -> Lenovo()
            Rom.isMeizu -> Meizu()
            Rom.isNubia -> Nubia()
            Rom.isSamsung -> Samsung()
            Rom.isAsus -> Asus()
            Rom.isFreeme -> Freeme()
            Rom.isCoolpad(context) -> Coolpad()
            else -> null
        }

        if (provider?.isSupport(context) == true) {
            return provider
        }
        // 中兴(zte)
        val msa = Msa()
        if (msa.isSupport(context)) {
            return msa
        }
        val gms = Gms()
        if (gms.isSupport(context)) {
            return gms
        }
        return Unsupported()
    }
}