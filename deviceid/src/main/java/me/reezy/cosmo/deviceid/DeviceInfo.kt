package me.reezy.cosmo.deviceid

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.Size
import android.view.WindowManager
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.MessageDigest
import java.util.Locale
import java.util.TimeZone
import kotlin.math.pow
import kotlin.math.sqrt


object DeviceInfo {

    private lateinit var info: Map<String, String>

    fun get(context: Context): Map<String, String> {
         if (!this::info.isInitialized) {
             info = collectDeviceInfo(context)
        }
        return info
    }

    private fun collectDeviceInfo(context: Context): Map<String, String> {
        val size = getScreenSize(context)
        return mapOf(
            "manufacturer" to Build.MANUFACTURER,
            "brand" to Build.BRAND,
            "model" to Build.MODEL,
            "board" to Build.BOARD,
            "hardware" to Build.HARDWARE,
            "resolution" to "${size.width}x${size.height}",
            "inches" to size.inches,
            "soc" to getSoc(),
            "abi" to Build.SUPPORTED_ABIS.joinToString(","),
            "cores" to Runtime.getRuntime().availableProcessors().toString(),

            "os" to "android ${Build.VERSION.RELEASE}",
            "sdk" to Build.VERSION.SDK_INT.toString(),
            "product" to Build.PRODUCT,
            "display" to Build.DISPLAY,
            "country" to Locale.getDefault().country,
            "language" to Locale.getDefault().language,
            "timezone" to TimeZone.getDefault().displayName,
            "harmony" to isHarmonyOS().toString(),
        )
    }

    private fun isHarmonyOS(): Boolean = kotlin.runCatching {
        val clazz = Class.forName("com.huawei.system.BuildEx")
        val method = clazz.getMethod("getOsBrand")
        val os = method.invoke(clazz) as String
        "harmony".equals(os, ignoreCase = true)
    }.getOrDefault(false)

    private fun getSoc(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            "${Build.SOC_MANUFACTURER} ${Build.SOC_MODEL}"
        } else {
            val keyHardware = "Hardware\t: "
            val lines = File("/proc/cpuinfo").readLines()
            lines.find { it.startsWith(keyHardware) }?.substring(keyHardware.length) ?: "unknown"
        }
    }

    private fun getScreenSize(context: Context): Size {
        val wm = context.getSystemService(WindowManager::class.java)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = wm.currentWindowMetrics.bounds
            Size(bounds.width(), bounds.height())
        } else {
            val point = Point()
            @Suppress("DEPRECATION")
            wm.defaultDisplay.getRealSize(point)
            Size(point.x, point.y)
        }
    }

    private val Size.inches: String
        get() {
            val dm = Resources.getSystem().displayMetrics
            val x = (width / dm.xdpi.toDouble()).pow(2.0)
            val y = (height / dm.ydpi.toDouble()).pow(2.0)
            return BigDecimal(sqrt(x + y)).setScale(2, RoundingMode.HALF_UP).toDouble().toString()
        }


//    private fun getProp(key: String, defValue: String = ""): String {
//        try {
//            val p = Runtime.getRuntime().exec("getprop $key")
//            BufferedReader(InputStreamReader(p.inputStream), 1024).use {
//                return it.readLine()
//            }
//        } catch (ex: IOException) {
//            Log.e("OoO", "Unable to read props", ex)
//        }
//        return defValue
//    }

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

}