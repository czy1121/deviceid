package me.reezy.cosmo.deviceid

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaDrm
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.security.MessageDigest
import java.util.UUID


@SuppressLint("HardwareIds")
object DeviceId {

    private val INVALID_ANDROID_ID = arrayOf("0000000000000000", "0123456789abcdef", "9774d56d682e549c")

    fun getAndroidId(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (INVALID_ANDROID_ID.contains(androidId)) {
            return ""
        }
        return androidId
    }

    /** widevineId, 通过安卓数字版权管理(DRM)框架获取的唯一设备ID */
    fun getWidevineId(): String {
        var drm: MediaDrm? = null
        try {
            val widevineUuid = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
            drm = MediaDrm(widevineUuid)
            return drm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID).hash()
        } catch (ex: Throwable) {
            //
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                drm?.close()
            } else {
                drm?.release()
            }
        }
        return ""
    }

    /** instanceId，持久化到内部存储的GUID */
    fun getInstanceId(context: Context): String {
        val key = "899c6a8d3b48a30e69e84dd338eba27b5b6a36d2"
        val sp = context.getSharedPreferences(context.packageName + "_deviceid", Context.MODE_PRIVATE)
        val id = sp.getString(key, null)
        if (!id.isNullOrEmpty()) {
            return id
        }
        val uuid = UUID.randomUUID().toString()
        sp.edit().putString(key, uuid).apply()
        return uuid
    }


    /** Google服务框架ID(Google Services Framework ID) */
    fun getGsfId(context: Context): String {
        val uri = Uri.parse("content://com.google.android.gsf.gservices")
        context.contentResolver.query(uri, null, null, arrayOf("android_id"), null)?.use {
            if (it.moveToFirst() && it.columnCount >= 2) {
                return it.getString(1)
            }
        }
        return ""
    }

//    @SuppressLint("PrivateApi")
//    private fun getSystemProperty(key: String, defValue: String? = null): String? {
//        try {
//            val clazz = Class.forName("android.os.SystemProperties")
//            val method = clazz.getMethod("get", String::class.java, String::class.java)
//            return method.invoke(clazz, key, defValue) as String?
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }

    private fun ByteArray.hash(algo: String = "sha1"): String {
        val data = MessageDigest.getInstance(algo).digest(this)
        val hex = StringBuilder()
        for (b in data) {
            if (b.toInt() and 0xff < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString(b.toInt() and 0xff))
        }
        return hex.toString()
    }
}