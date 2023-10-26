package me.reezy.cosmo.deviceid.oaid

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import java.util.*


@SuppressLint("PrivateApi")
object Rom {

    val manufacturer: String = Build.MANUFACTURER.lowercase(Locale.ROOT)

    val isHuawei: Boolean get() = manufacturer == "huawei"
    val isXiaomi: Boolean get() = manufacturer == "xiaomi"
    val isOppo: Boolean get() = manufacturer == "oppo"
    val isVivo: Boolean get() = manufacturer == "vivo"


    // 魅族手机
    val isMeizu: Boolean get() = manufacturer == "meizu"
    // 一加
    val isOnePlus: Boolean get() = manufacturer == "oneplus"
    // 华硕
    val isAsus: Boolean get() = manufacturer == "asus"
    // 中兴
    val isZte: Boolean get() = manufacturer == "zte"
    // 黑鲨
    val isBlackShark: Boolean get() = manufacturer == "blackshark"
    // 三星
    val isSamsung: Boolean get() = manufacturer == "samsung"
    // 摩托罗拉
    val isMotolora: Boolean get() = manufacturer == "motorola"
    // 努比亚
    val isNubia: Boolean get() = manufacturer == "nubia"
    // 联想
    val isLenovo: Boolean get() = manufacturer == "lenovo"
    // 卓易
    val isFreeme: Boolean get() = !getSystemProperty("ro.build.freeme.label").isNullOrEmpty()

    // 酷派
    fun isCoolpad(context: Context): Boolean = context.hasPackage("com.coolpad.deviceidsupport")


    @SuppressLint("PrivateApi")
    fun getSystemProperty(key: String, defValue: String? = null): String? {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val method = clazz.getMethod("get", String::class.java, String::class.java)
            return method.invoke(clazz, key, defValue) as String?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}